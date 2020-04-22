#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>

#define PORT 5566
#define MAX_LENGH 1024

int main() 
{
	int sock = 0, valread;
	struct sockaddr_in servaddr;
	char *ping = "ping";
	char buffer[MAX_LENGH];

	/*
	IF_INET = IPv4
	SOCK_STREAM = TCP
	*/

	if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0) 
	{
		perror("socket creation failed");
		exit(-1);
	}

	servaddr.sin_family = AF_INET;
	servaddr.sin_port = htons(PORT);

 	if(inet_pton(AF_INET, "127.0.0.1", &servaddr.sin_addr)<=0)  
    { 
        printf("Invalid address/ Address not supported\n"); 
        exit(-1);
    } 

	if (connect(sock, (struct sockaddr *)&servaddr, 
		sizeof(servaddr)) < 0)
	{
		perror("connection failed");
		exit(-1);
	}

	while (1)
	{
		send(sock, ping, strlen(ping), 0);
		valread = read(sock, buffer, MAX_LENGH);
		buffer[valread] = '\0';
		printf("%s\n", buffer);
		sleep(1);
	}

	return 0;
}