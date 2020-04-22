package networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PingPongSelectionServer {
	private static final int PORT_NUMBER = 5454;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Selector selector = Selector.open();
		System.out.println("ping pong with selector");
		System.out.println("selector open: " + selector.isOpen());
		
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		InetSocketAddress hostAddress = new InetSocketAddress("localhost", PORT_NUMBER);
		
		serverSocket.bind(hostAddress);
		serverSocket.configureBlocking(false);
		
		int ops = serverSocket.validOps();
		SelectionKey selectKey = serverSocket.register(selector, ops);
		
		while (true) {
			selector.select();
			
			Set<SelectionKey> slelctedKeysSet = selector.selectedKeys();
			Iterator<SelectionKey> iter = slelctedKeysSet.iterator();
			
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				
				if (key.isAcceptable()) {
					SocketChannel client = serverSocket.accept();
					client.configureBlocking(false);
					
					client.register(selector, SelectionKey.OP_READ);
					
					System.out.println("accepted new connection from client " + client);
				}
				else if (key.isReadable()) {
					SocketChannel client = (SocketChannel) key.channel();
					ByteBuffer buffer = ByteBuffer.allocate(256);
					client.read(buffer);
					String output = new String(buffer.array()).trim();
					System.out.println(output);
					client.register(selector, SelectionKey.OP_WRITE);
				}
				else if (key.isWritable()) {
					SocketChannel client = (SocketChannel) key.channel();
					byte [] message = new String("pong").getBytes();
			        ByteBuffer buffer = ByteBuffer.wrap(message);
			        client.write(buffer);
			
			        buffer.clear();
			        Thread.sleep(1000);
			        
			        client.register(selector, SelectionKey.OP_READ);
				}
			}
			
			iter.remove();
		}
	}
}