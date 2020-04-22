package chatserver;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import colors.Colors;
import validator.Validator;

public class UserManagment {
	private final String WELCOME_MESSAGE = "welcome, please enter your name: ";
	private final String JOINED_THE_GROUP_MESSAGE = " joined the group";
	private final String LEFT_THE_GROUP_MESSAGE = " left the group";
	private Validator validator = new ChatServerValidator();
    private HashMap<ComObj, Client> clients = new HashMap<>();
    private Consumer<ComObj> newClient = (comObj) -> {
		clients.put(comObj, null);
		try {
			sendMassege(comObj, WELCOME_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
    };
    
    private Consumer<ComObj> disconnectClient = (comObj) -> {
    	String messageToAll;
		try {
			messageToAll = new String(validator.wrap((clients.get(comObj).getNameWhite() + LEFT_THE_GROUP_MESSAGE).getBytes()));
			clients.remove(comObj);
	    	
			for (Entry<ComObj, Client> entry : clients.entrySet()) {
				try {
					entry.getKey().handleMessage(messageToAll);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ProtocolException e1) {
			e1.printStackTrace();
		}    	
    };
    
    private BiConsumer<ComObj, String> message = (comObj, message) -> {
    	if (clients.get(comObj) == null) {
			try {
				handleNewClient(comObj, message);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    	String senderName = clients.get(comObj).getName();

		try {
			String messageToAll = new String(validator.wrap((senderName + ": " + message).getBytes()));
			for (Entry<ComObj, Client> entry : clients.entrySet()) {
				try {
					entry.getKey().handleMessage(messageToAll);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ProtocolException e1) {
			e1.printStackTrace();
		}
    };
	
	public void registerToEvents(Server server) {
		server.registerNewClient(newClient);
		server.registerClientDisconnect(disconnectClient);
		server.registerNewMessage(message);
	}
	
	private void sendMassege(ComObj comObj, String message) throws IOException {
		if (clients.get(comObj) == null) { // new client case
			comObj.handleMessage(new String(validator.wrap(message.getBytes())));
			return;
		}

		String senderName = clients.get(comObj).getName();
		for (Entry<ComObj, Client> entry : clients.entrySet()) {
			entry.getKey().handleMessage(senderName + ": " + message);
		}
	}
	
	private void handleNewClient(ComObj comObj, String name) throws IOException {
		Client newClient = new Client(name);
		
		clients.replace(comObj, newClient);

		for (Entry<ComObj, Client> entry : clients.entrySet()) {
			if (!entry.getKey().equals(comObj)) {
				String messageToAllUsers = new String(validator.wrap((name + JOINED_THE_GROUP_MESSAGE).getBytes()));
				
				entry.getKey().handleMessage(messageToAllUsers);
			}
		} 
	}
	
	private class Client {
		private String name;
		private String color;
		
		public Client(String name) {
			this.name = name;
			color = RandomColor.getRandomColor();
		}
		
		public String getName() {
			return color + name + Colors.ANSI_RESET;
		}
		
		public String getNameWhite() {
			return name;
		}
	}
	
	private static class RandomColor {
		private static Random random = new Random();
		private static String[] colors = {
				Colors.ANSI_BLUE, Colors.ANSI_CYAN, Colors.ANSI_PURPLE, 
				Colors.ANSI_RED, Colors.ANSI_YELLOW 
		};
		
		public static String getRandomColor() {
			int randIndex = random.nextInt(colors.length);
			
			return colors[randIndex];
		}
	}
	
}
