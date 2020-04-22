package chatserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import validator.Validator;

public class Client {
	private final static int PORT_NUMBER = 7700;
	private final static int BUFFER_SIZE = 256;
	private final InetSocketAddress hostAddress;
	private final SocketChannel client;
	private final Scanner scanner = new Scanner(System.in);
    private Thread listenerThread;
    private Validator validator = new ChatServerValidator();
    
    public Client() throws IOException {
    	hostAddress = new InetSocketAddress("localhost", PORT_NUMBER);
    	client = SocketChannel.open(hostAddress);
		listenerThread = new Thread(new Listner());
    } 
    
	public void start() throws IOException, InterruptedException {
		listenerThread.start();
		
		while (true) {
			String echoString = scanner.nextLine();
			ByteBuffer buffer = ByteBuffer.wrap(validator.wrap(echoString.getBytes()));
			client.write(buffer);
		}
	}
  
	class Listner extends Thread {
		@Override
		public void run() {
			while (true) {
    		  ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
	    	  try {
				if (-1 == client.read(buffer)) { 
					System.out.println("serever is down!");
					return;
				}
				
		    	System.out.println(new String(buffer.array(), 0, buffer.position()));
	    	  }	 catch (IOException e) {
				e.printStackTrace();
	    	  }
			}
		}
	}
}