package server.main;

import java.util.Arrays;
import message.Message;
/**
 * ServerLogin gets a username and password from a client and checks to see if registering
 * and/or login is possible.
 * @author David Sandh, Lucas Knutsäter.
 *
 */
public class ServerLogin {
	/**
	 * Check if login is possible
	 * @param username Username to login with
	 * @param password Password to login with
	 * @return boolean
	 */
	protected static boolean loginCheck(String username, String password) {
		ServerPsqlConnection psql = new ServerPsqlConnection();
		System.out.println(psql.checkIfAvailable(username));
		if(!psql.checkIfAvailable(username)) {
			String salt = psql.selectSalt(username);
			String[] byteValues = salt.substring(1, salt.length() - 1).split(",");
			byte[] bytes = new byte[byteValues.length];
			for (int i=0; i<bytes.length; i++) {
				bytes[i] = Byte.parseByte(byteValues[i].trim());     
			}
			HashPassword hashPassword = new HashPassword();
			if(hashPassword.comparePasswords(password, bytes).equals(psql.selectPassword(username))) {
				ServerController.logHandler("Successful login by " + username);
				return true;
			}
		}
		ServerController.logHandler("login failed by " + username);
		return false;
	}
	
	/**
	 * Method to register username and password
	 * @param message object containing username and password
	 * @return boolean
	 */
	protected static boolean register(Message message) {
		String username = message.getUsername();
		ServerPsqlConnection psql = new ServerPsqlConnection();
		if(!psql.checkIfAvailable(username)) {
			ServerController.logHandler(username + " is taken. Register failed");
			return false;
		}
		HashPassword hashPassword = null;
		try {
			hashPassword = new HashPassword(message.getData());
		} catch (Exception e) {
			
		}
		psql.insertRegister(username, hashPassword.getHash() , Arrays.toString(hashPassword.getSalt()));
		ServerController.logHandler(username + " registered");
		return true;
	}
}