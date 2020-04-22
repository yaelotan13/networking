package networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PingPongServerTCP {
	private final static int PORT_NUMBER = 5000;
	
	public static void main(String[] args) {
		try {
			System.out.println("TCP server is up");
			ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
			Socket socket = serverSocket.accept();
			System.out.println("client connected");
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			boolean pingIsReady = false;
			
			while (true) {
				writer.write("pong");
				writer.newLine();
				writer.flush();
				while (!pingIsReady) {
					if (reader.ready()) {
						System.out.println(reader.readLine());
						Thread.sleep(1000);
						pingIsReady = true;
					}
				}
				pingIsReady = false;
			}
			
		} catch (IOException | InterruptedException e) {
			System.out.println("exception occured: " + e);
		}
	}
}
