package serverMain;

import java.util.ArrayList;
import java.util.HashMap;

import message.Message;

public class ServerMessageHandler {
	private HashMap<String, ArrayList<String>> messageBuffer = new HashMap<String, ArrayList<String>>();
	
	protected void put(String username, String message) {
		if (!messageBuffer.containsKey(username)) {
			messageBuffer.put(username, new ArrayList<String>());
			System.out.println("nyckelfinnsinte  " + username + " : " + messageBuffer.get(username).toString());
		}
			messageBuffer.get(username).add(message);	
			System.out.println("nyckelfinns "  + username + " : " + messageBuffer.get(username).toString());
	}
	protected String remove(String username) {
		ArrayList<String> tempList = messageBuffer.get(username);
		if(!tempList.isEmpty()) {
			return tempList.remove(0);
		}
		return null;
	}
	public static void main(String [] args) {
		ServerMessageHandler test = new ServerMessageHandler();
		test.put("nyckel", "value");
		test.put("nyckel", "value1");
		test.put("nyckel", "value2");
		test.put("nyckel", "value3");
		test.put("nyckel2", "value0");
		test.put("nyckel2", "value1");
		test.put("nyckel2", "value2");
		String testString = test.remove("nyckel");
		while(!(testString == null)){
			System.out.println(testString);
			testString = test.remove("nyckel");
		}
		
		
	}
}
