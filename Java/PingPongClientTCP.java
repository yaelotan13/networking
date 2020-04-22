package networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class PingPongClientTCP {
	private final static int PORT_NUMBER = 5000;
	
	public static void main(String[] args) {
		
		try {
			Socket clientSocket = new Socket("localhost", PORT_NUMBER);
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			Scanner scanner = new Scanner(System.in);
			boolean pongIsReady = false;
			
			System.out.println("TCP client is ready");
			if (scanner.next().equals("start")) {
				while (true) {
					writer.write("ping");
					writer.newLine();
					writer.flush();
					while (!pongIsReady) {
						if (reader.ready()) {
							System.out.println(reader.readLine());
							Thread.sleep(1000);
							pongIsReady = true;
						}
					}
					pongIsReady = false;
				}
			}
			

		} catch (IOException | InterruptedException e) {
			System.out.println("exception occured: " + e);
		}
	}
}
