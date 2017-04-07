package serverMain;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import message.Message;
/**
 * ServerController
 * @author Lucas Knutsäter & David Sandh
 *
 */
public class ServerController {
	private Server server;
	private static ServerGUI serverGUI;
	private static Logger log;
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
			return new Message(Message.STATUS, ServerLogin.loginCheck(message.getUsername(), message.getPassword()));
		} else if(message.getType() == 1) {
			return new Message(Message.STATUS, ServerLogin.register(message));
		} else if (message.getType() == 2) {
			return null;
		}
		return null;
		
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
	public static void logHandler(String logMessage) {
		log.info(logMessage + "\n");
		serverGUI.append(logMessage);
	}
}
