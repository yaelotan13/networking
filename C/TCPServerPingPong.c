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
	int serverfd, new_socket, valread;
	struct sockaddr_in address;
	char buffer[MAX_LENGH];
	char *pong = "pong";
	int opt = 1;
	int addrlen = sizeof(address);

	//create socket file descriptor
	if ((serverfd = socket(AF_INET, SOCK_STREAM, 0)) <= 0)
	{
		perror("socket creation failed");
		exit(-1);
	}

	//atatching socket to port 5566 (optional function)
	if (setsockopt(serverfd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, 
		&opt, sizeof(opt))) 
	{
		perror("socketop failure");
		exit(-1);
	}

	//initillize the struct 
	address.sin_family = AF_INET;
	address.sin_addr.s_addr = INADDR_ANY;
	address.sin_port = htons(PORT);

	if (bind(serverfd, (struct sockaddr *)&address, sizeof(address)) < 0)
	{
		perror("bind failed");
		exit(-1);
	}

	if(listen(serverfd, 3) < 0) 
	{
		perror("listen error");
		exit(-1);
	}

	printf("server listening\n");

	if((new_socket = accept(serverfd, (struct sockaddr *)&address, 
		(socklen_t *)&addrlen)) < 0)
	{
		perror("accept error");
		exit(-1);
	}

	while (1) 
	{
		valread = read(new_socket, buffer, MAX_LENGH);
		buffer[valread] = '\0';
		printf("%s\n", buffer);
		sleep(1);
		send(new_socket, pong, strlen(pong), 0);
	}

	return 0;
}