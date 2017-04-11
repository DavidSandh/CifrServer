package server.main;

import java.util.ArrayList;
import java.util.HashMap;
import message.Message;

 /**
  * 
  * @author David Sandh & Lucas Knutsäter
  *
  */
public class ServerMessageHandler {
	private static HashMap<String, ArrayList<Message>> messageBuffer = new HashMap<String, ArrayList<Message>>();
	
	/**
	 * 
	 * @param username
	 * @param message
	 */
	protected static void put(String username, Message message) {
		if (!messageBuffer.containsKey(username)) {
			messageBuffer.put(username, new ArrayList<Message>());
		}
		messageBuffer.get(username).add(message);	
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	protected static Message remove(String username) {
		ArrayList<Message> tempList = messageBuffer.get(username);
		if(!tempList.isEmpty()) {
			return tempList.remove(0);
		}
		return null;
	}
}	