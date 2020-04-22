package chatserver;

import java.io.IOException;

public class MainServer {
	public static void main(String[] args) throws IOException {
		Server server = new TCPServer();
		UserManagment userManagment = new UserManagment();
		
		userManagment.registerToEvents(server);
		server.raiseServer();
	}
}
