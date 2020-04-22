package chatserver;

import java.io.IOException;

public interface ComObj {
	void handleMessage(String message) throws IOException;
}
