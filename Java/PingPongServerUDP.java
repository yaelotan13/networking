package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PingPongServerUDP {
	private final static int PORT_NUMBER = 6050;
	
	public static void main(String[] args) {
		System.out.println("UDP server is up");
		try {
			DatagramSocket serverDatagramSocket = new DatagramSocket(PORT_NUMBER);
			String pong = "pong";
			byte[] buffer = pong.getBytes();
			DatagramPacket DatagramPacket = null;
			byte[] reciveingBuffer = new byte[65535];
			
			while (true) {
				DatagramPacket = new DatagramPacket(reciveingBuffer, reciveingBuffer.length);
				serverDatagramSocket.receive(DatagramPacket);
				String messege = new String(DatagramPacket.getData());
				System.out.println(messege);
				
				InetAddress address = DatagramPacket.getAddress();
				int port = DatagramPacket.getPort();
				DatagramPacket = new DatagramPacket(buffer, buffer.length, address, port);
				serverDatagramSocket.send(DatagramPacket);
			}
			
		} catch (IOException e) {
			System.out.println("exception occured: " + e);
		}
	}
	
    public static StringBuilder data(byte[] a) { 
        if (a == null) { return null; }
        
        StringBuilder ret = new StringBuilder(); 
        int i = 0; 
        while (a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        
        return ret; 
    }
}
