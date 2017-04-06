package serverMain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 * ServerController
 * @author Lucas Knutsäter & David Sandh
 *
 */
public class ServerController {
	private Server server;
	private ServerGUI serverGUI;
	private Logger log;
	private FileHandler fileHandle;
	/**
	 * Constructor which adds controller to serverGUI and server
	 * @param server instance of Server
	 * @param serverGUI instance of ServerGUI
	 */
	public ServerController(Server server, ServerGUI serverGUI) {
		this.server = server;
		this.serverGUI = serverGUI;
		serverGUI.addController(this);
		server.addController(this);
		startLog();
	}
	/**
	 * method to start server
	 */
	public void startServer() {
		server.startServer();
	}
	/**
	 * method to stop server
	 */
	public void stopServer() {
		server.stopServer();
	}
	/**
	 * Check what type object is
	 * @param object object to check
	 * @return message to return
	 */
	public Message checkType(Object object) {
		Message message = (Message) object;
		if(message.getType() == 0) {
			return new Message(Message.STATUS, loginCheck(message.getUsername(), message.getPassword()));
		} else if(message.getType() == 1) {
			return new Message(Message.STATUS, register(message));
			
		} else if (message.getType() == 2) {
			return null;
		}
		return null;
		
	}
	/**
	 * Method to register username and password
	 * @param message object containing username and password
	 * @return boolean
	 */
	private boolean register(Message message) {
		String username = message.getUsername();
		if(!checkIfAvailable(username)) {
			logHandler(username + " is taken. Register failed");
			return false;
		}
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("files/registeredUsers.txt", true),"UTF-8"))) {
			writer.write(message.getUsername()+","+message.getPassword() + "\n");
//			writer.append
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logHandler(username + " registered");
		return true;
	}
	/**
	 * Checks if username exist in file.
	 * @param username username to check
	 * @return boolean
	 */
	private boolean checkIfAvailable(String username) {
		try(BufferedReader reader = new BufferedReader(new FileReader("files/registeredUsers.txt"))) {
			while(reader.ready()) {
				String line = reader.readLine();
				String[] lineSplit = line.split(",");
				if(username.equals(lineSplit[0])) {
					return false;
				}
			}
		} catch(FileNotFoundException e1) {
			e1.printStackTrace();
		} catch(IOException e2) {
			e2.printStackTrace();
		}
		return true;
	}
	/**
	 * Check if login is possible
	 * @param username Username to login with
	 * @param password Password to login with
	 * @return boolean
	 */
	private boolean loginCheck(String username, String password) {
		try(BufferedReader reader = new BufferedReader(new FileReader("files/registeredUsers.txt"))) {
			while(reader.ready()) {
				String line = reader.readLine();
				String[] lineSplit = line.split(",");
				if(username.equals(lineSplit[0])) {
					if(password.equals(lineSplit[1])) {
						logHandler("Successful login by " + username);
						return true;
					}
				}
			}
		} catch(FileNotFoundException e1) {
			e1.printStackTrace();
		} catch(IOException e2) {
			e2.printStackTrace();
		}
		logHandler("login failed by " + username);
		return false;
	}
	/**
	 * Method to start logging
	 */
	private void startLog() {
		log = Logger.getLogger("Log");
		try {
			fileHandle = new FileHandler("log.txt");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileHandle.setFormatter(new SimpleFormatter());
		log.addHandler(fileHandle);
	}
	/**
	 * Method to add log message
	 * @param logMessage Message to log
	 */
	public void logHandler(String logMessage) {
		log.info(logMessage + "\n");
		serverGUI.append(logMessage);
	}
}
