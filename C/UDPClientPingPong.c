#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>

#define PORT 4000
#define MAX_LENGTH 1024

int main() 
{
	int socketfd;
	char buffer[MAX_LENGTH];
	char *ping = "ping";
	struct sockaddr_in servaddr;

	/*
	carete socket file descriptor
	AF_INET = IPv4
	SOCK_DGRAM = UDP
	0 = use the default protocol for this address family (UDP)
	return value: -1 on error or the new socket 
	*/
	if ((socketfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0) 
	{
		perror("socket creation failed");
		exit(EXIT_FAILURE);
	}

	memset(&servaddr, 0, sizeof(servaddr)); //fill with 0

	//fill the server information 
	servaddr.sin_family = AF_INET;
	servaddr.sin_port = htons(PORT);
	servaddr.sin_addr.s_addr = INADDR_ANY;

	int n, len;
	while (1) 
	{
		sendto(socketfd, (const char *)ping, strlen(ping), 
		MSG_CONFIRM, (const struct sockaddr *)&servaddr, sizeof(servaddr));
		
		n = recvfrom(socketfd, (char *)buffer, MAX_LENGTH, 
		MSG_WAITALL, (struct sockaddr *)&servaddr, &len);
		buffer[n] = '\0';
		printf("%s\n", buffer);
		sleep(1);
	}


	close(socketfd);

	return 0;
}