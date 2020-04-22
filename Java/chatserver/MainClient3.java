package chatserver;

import java.io.IOException;

public class MainClient3 {
	public static void main(String[] args) throws IOException, InterruptedException {
		Client client = new Client();
		
		client.start();
	}
}
