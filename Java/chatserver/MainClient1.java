package chatserver;

import java.io.IOException;

public class MainClient1 {
	public static void main(String[] args) throws IOException, InterruptedException {
		Client client = new Client();
		
		client.start();
	}
}
