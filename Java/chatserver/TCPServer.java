package chatserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import colors.Colors;
import validator.Validator;

public class TCPServer implements Server {
	private final static int PORT_NUMBER = 7700;
	private final static int BUFFER_SIZE = 256;
	private final Selector selector = Selector.open();
	private ServerSocketChannel serverSocket = ServerSocketChannel.open();
	private Consumer<ComObj> newClient;
	private Consumer<ComObj> disconnectClient;
	private BiConsumer<ComObj, String> newMessage;
	private Validator validator = new ChatServerValidator();
	
	public TCPServer() throws IOException {
		registerServerSocket();  
	}
	
	@Override
	public void raiseServer() throws IOException {
		System.out.println(Colors.ANSI_CYAN + "C" + Colors.ANSI_PURPLE +  "H" + 
				Colors.ANSI_RED + "A" + Colors.ANSI_WHITE + "T " + Colors.ANSI_CYAN +
				"S" + Colors.ANSI_WHITE + "E" + Colors.ANSI_GREEN + "R" + Colors.ANSI_PURPLE +
				"V" + Colors.ANSI_CYAN + "E" + Colors.ANSI_WHITE +"R");
		
		while (true) {
			selector.select();
			Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
			
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
	
				if (key.isAcceptable()) { 
					System.out.println("client connected");
					registerClientSocket(key);
				}
				if (key.isReadable()) { 
					String message = getMessage(key); 
					if (message != null) {
						newMessage.accept((ComObj)(key.attachment()), message);
					}
				}
				
				iter.remove();
			}	
		}
	}

	private class comObjImpl implements ComObj {
		private SocketChannel socket;
		
		public comObjImpl(SocketChannel socket) {
			this.socket = socket;
		}
		
		@Override
		public void handleMessage(String message) throws IOException {
			if (!validator.isValid(message.getBytes())) { throw new ProtocolException(); }
			ByteBuffer buffer = ByteBuffer.wrap(validator.unwrap(message.getBytes()));
			
			socket.write(buffer);
		}
	}
	
	@Override
	public void registerNewClient(Consumer<ComObj> newClient) {
		this.newClient = newClient;
	}

	@Override
	public void registerNewMessage(BiConsumer<ComObj, String> newMessage) {
		this.newMessage = newMessage;
	}

	@Override
	public void registerClientDisconnect(Consumer<ComObj> disconnectClient) {
		this.disconnectClient = disconnectClient;
	}
	
	private String getMessage(SelectionKey key) throws IOException, ProtocolException {
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		SocketChannel client = (SocketChannel) key.channel();
		
		if (client.read(buffer) == -1) {
			diconnectClient(key);
			return null;
		}
		
		String messageAfterVlidation = new String(validator.unwrap(buffer.array()));
		
		return messageAfterVlidation;
	}
	

	private void diconnectClient(SelectionKey key) throws IOException {
		disconnectClient.accept((ComObj)(key.attachment()));
		key.channel().close();
	}
	
	private void registerServerSocket() throws IOException {
		serverSocket.bind(new InetSocketAddress("localhost", PORT_NUMBER));
		serverSocket.configureBlocking(false);
		serverSocket.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	private void registerClientSocket(SelectionKey key) throws IOException {
		SocketChannel client = ((ServerSocketChannel)(key.channel())).accept();
		client.configureBlocking(false);
		SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
		
		comObjImpl comObj = new comObjImpl(client);
		clientKey.attach(comObj);
		newClient.accept(comObj);
	}
}
	
