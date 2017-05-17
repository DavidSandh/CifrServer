package server.main;

import java.util.ArrayList;
import java.util.HashMap;
import message.Message;

 /**
  * A buffer which contains ArrayLists of messages in a HashMap
  * @author David Sandh & Lucas Knutsäter
  *
  */
public class ServerMessageHandler {
	private static HashMap<String, ArrayList<Message>> messageBuffer = new HashMap<String, ArrayList<Message>>();
	
	/**
	 * Puts a message in the ArrayList in the HashMap with key username
	 * @param username HashMap key 
	 * @param message Message to add
	 */
	public synchronized static void put(String username, Message message) {
		if (!messageBuffer.containsKey(username)) {
			messageBuffer.put(username, new ArrayList<Message>());
		}
		messageBuffer.get(username).add(message);	
	}
	
	/**
	 * Removes a message in the ArrayList in the HashMap with key username
	 * @param username HashMap key
	 * @return message Message to return. Null if no message to return.
	 */
	public  synchronized static Message remove(String username) {
		ArrayList<Message> tempList = messageBuffer.get(username);
		if(!tempList.isEmpty()) {
			return tempList.remove(0);
		}
		return null;
	}
}	