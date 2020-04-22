package chatserver;

import java.net.ProtocolException;
import java.nio.ByteBuffer;

import validator.Validator;

public class ChatServerValidator implements Validator {
	private static final int NUM__TOKENS = 2;
	private final String TOKENS = "#@";
	
	@Override
	public byte[] wrap(byte[] message) throws ProtocolException {
		return ByteBuffer.wrap((TOKENS + new String(message) + TOKENS).getBytes()).array();
	}

	@Override
	public byte[] unwrap(byte[] message) throws ProtocolException {
		if (!isValid(message)) {
			throw new ProtocolException();
		}
		
		String ret = new String(message).trim();
		
		return ret.substring(NUM__TOKENS, ret.length() - NUM__TOKENS).getBytes();
	}

	@Override
	public boolean isValid(byte[] message) {
		String strMessage = new String(message).trim();

		return (message.length > NUM__TOKENS * 2 &&
			strMessage.startsWith(TOKENS) &&
			strMessage.endsWith(TOKENS)); 
	}

}
