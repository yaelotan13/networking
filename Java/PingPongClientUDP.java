package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class PingPongClientUDP {
	private final static int PORT_NUMBER = 6050;
	//private static InetAddress address = InetAddress.getByName("localhost");
	
	public static void main(String[] args) {
		try {
			DatagramSocket clientDatagramSocket = new DatagramSocket();
			Scanner scanner = new Scanner(System.in);
			String ping = "ping";
			byte[] buffer = ping.getBytes();
			DatagramPacket DpReceive = null;
			byte[] reciveingBuffer = new byte[65535];
			 
			System.out.println("UDP client is ready");
			if (scanner.next().equals("start")) {
				while (true) {
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), PORT_NUMBER);
					clientDatagramSocket.send(packet);
					
					packet = new DatagramPacket(reciveingBuffer, reciveingBuffer.length);
					clientDatagramSocket.receive(packet);
					String messege = new String(packet.getData());
					System.out.println(messege);
				}
			}
		} catch (IOException e) {
			System.out.println("exception occured: " + e);
		}
	}
}
