package serverMain;

import java.util.ArrayList;
import java.util.HashMap;

import message.Message;

 /**
  * ÄNDRA STRING TILL MESSAGE
  * @author kungs
  *
  */
public class ServerMessageHandler {
	private static HashMap<String, ArrayList<Message>> messageBuffer = new HashMap<String, ArrayList<Message>>();
	
	
	protected static void put(String username, Message message) {
		if (!messageBuffer.containsKey(username)) {
			messageBuffer.put(username, new ArrayList<Message>());
			System.out.println("nyckelfinnsinte  " + username + " : " + messageBuffer.get(username).toString()); //Remove
		}
			messageBuffer.get(username).add(message);	
			System.out.println("nyckelfinns "  + username + " : " + messageBuffer.get(username).toString()); //remove
	}
	protected static Message remove(String username) {
		ArrayList<Message> tempList = messageBuffer.get(username);
		if(!tempList.isEmpty()) {
			return tempList.remove(0);
		}
		return null;
	}
	/**
	 * TA BORT SEN
	 * @param args
	 */
//	public static void main(String [] args) {
//		ServerMessageHandler test = new ServerMessageHandler();
//		test.put("nyckel", "value");
//		test.put("nyckel", "value1");
//		test.put("nyckel", "value2");
//		test.put("nyckel", "value3");
//		test.put("nyckel2", "value0");
//		test.put("nyckel2", "value1");
//		test.put("nyckel2", "value2");
//		String testString = test.remove("nyckel");
//		while(!(testString == null)){
//			System.out.println(testString);
//			testString = test.remove("nyckel");
//		}
//	}
}
