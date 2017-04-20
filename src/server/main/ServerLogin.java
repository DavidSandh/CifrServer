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
		try(BufferedReader reader = new BufferedReader(new FileReader("files/registeredUsers.txt"))) {
			while(reader.ready()) {
				String tempLine = reader.readLine();
				String[] tempLineSplit = tempLine.split("\\.");
				if(username.equals(tempLineSplit[0])) {
					String[] byteValues = tempLineSplit[2].substring(1, tempLineSplit[2].length() - 1).split(",");
					byte[] bytes = new byte[byteValues.length];
					for (int i=0; i<bytes.length; i++) {
					   bytes[i] = Byte.parseByte(byteValues[i].trim());     
					}
					HashPassword hashPassword = new HashPassword();
					if(hashPassword.comparePasswords(password, bytes).equals(tempLineSplit[1])) {
						ServerController.logHandler("Successful login by " + username);
						return true;
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		ServerController.logHandler("login failed by " + username);
		return false;
	}
	
	/**
	 * Checks if username exist in file.
	 * @param username username to check
	 * @return boolean
	 */
	protected static boolean checkIfAvailable(String username) {
		try(BufferedReader reader = new BufferedReader(new FileReader("files/registeredUsers.txt"))) {
			while(reader.ready()) {
				String tempLine = reader.readLine();
				String[] tempLineSplit = tempLine.split("\\.");
				if(username.equals(tempLineSplit[0])) {
					return false;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Method to register username and password
	 * @param message object containing username and password
	 * @return boolean
	 */
	protected static boolean register(Message message) {
		String username = message.getUsername();
		if(!checkIfAvailable(username)) {
			ServerController.logHandler(username + " is taken. Register failed");
			return false;
		}
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("files/registeredUsers.txt", true),"UTF-8"))) {
			try {
				HashPassword hashPassword = new HashPassword(message.getPassword());
				writer.write(message.getUsername()+"."+ hashPassword.getHash() + "." + Arrays.toString(hashPassword.getSalt()) + "\n");
			} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ServerController.logHandler(username + " registered");
		return true;
	}
}