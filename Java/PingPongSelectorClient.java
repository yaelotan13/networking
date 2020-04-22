package networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class PingPongSelectorClient {
	private static final int PORT_NUMBER = 5454;
	
	public static void main(String[] args) throws IOException, InterruptedException {
	      InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5454);
	      SocketChannel client = SocketChannel.open(hostAddress);
	      Scanner scanner = new Scanner(System.in);
	      
	      System.out.println("client sending messeges to the server");
	      
	      if (scanner.next().equals("start")) {
	    	  while (true) {
		    	  byte[] ping = new String("ping").getBytes();
		    	  ByteBuffer buffer = ByteBuffer.wrap(ping);
		    	  client.write(buffer);
		    	  
		    	  buffer.clear();
		    	  Thread.sleep(1000);
		    	  
		    	  buffer = ByteBuffer.allocate(256);
		    	  client.read(buffer);
		    	  String output = new String(buffer.array()).trim();
		    	  System.out.println(output);
	    	  }
	      }
	}
}
