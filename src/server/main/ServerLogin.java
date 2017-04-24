package server.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import message.Message;

public class ServerLogin {
	/**
	 * Check if login is possible
	 * @param username Username to login with
	 * @param password Password to login with
	 * @return boolean
	 */
	protected static boolean loginCheck(String username, String password) {
				ServerPsqlConnection psql = new ServerPsqlConnection();
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
			hashPassword = new HashPassword(message.getPassword());
		} catch (Exception e) {
			
		}
		psql.insert(username, hashPassword.getHash() , Arrays.toString(hashPassword.getSalt()));
		ServerController.logHandler(username + " registered");
		return true;
	}
	
	
	public static void main (String [] args) {
		ServerLogin login = new ServerLogin();
		login.register(new Message(Message.REGISTER, "Lucas" , "password"));
	}
}