#include <stdio.h>
#include <stdlib.h>
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
	char *pong = "pong";
	struct sockaddr_in servaddr, cliadd;

	//create socket file descriptor
	if ((socketfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0) 
	{
		printf("socket creation failed\n");
		perror("socket creation failed");
		exit(-1);
	}

	memset(&servaddr, 0, sizeof(servaddr));
	memset(&cliadd, 0, sizeof(cliadd));

	//filling the server information
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = INADDR_ANY;
	servaddr.sin_port = htons(PORT);

	//bind the socket with the server address
	if (bind(socketfd, (const struct sockaddr *)&servaddr, 
		sizeof(servaddr)) < 0)
		{
			printf("bind failed\n");
			perror("bind failed");
			exit(-1);
		} 

	printf("waiting for datagram\n");
	int len, n;

	while (1) 
	{
		n = recvfrom(socketfd, (char *)buffer, MAX_LENGTH, MSG_WAITALL,
		(struct sockaddr *)&cliadd, &len);
		buffer[n] = '\0';
		printf("%s\n", buffer);
		sendto(socketfd, (const char *)pong, strlen(pong), MSG_CONFIRM, 
			(const struct sockaddr *)&cliadd, len);
		sleep(1);
	}

	close(socketfd);

	return 0;

}