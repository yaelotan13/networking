package chatserver;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Server {
	void raiseServer() throws IOException;
	void registerNewClient(Consumer<ComObj> newClient);
	void registerNewMessage(BiConsumer<ComObj,String> newMessage);
	void registerClientDisconnect(Consumer<ComObj> client);
}