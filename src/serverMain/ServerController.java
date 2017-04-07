package serverMain;

import java.io.IOException;
import java.time.LocalDateTime;
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
	protected ServerController(Server server, ServerGUI serverGUI) {
		this.server = server;
		this.serverGUI = serverGUI;
		serverGUI.addController(this);
		server.addController(this);
		startLog();
	}
	/**
	 * method to start server
	 */
	protected void startServer() {
		server.startServer();
	}
	/**
	 * method to stop server
	 */
	protected void stopServer() {
		server.stopServer();
	}
	/**
	 * Check what type object is
	 * @param object object to check
	 * @return message to return
	 */
	protected Message checkType(Object object) {
		Message message = (Message) object;
		if(message.getType() == 0) {
			return new Message(Message.STATUS, ServerLogin.loginCheck(message.getUsername(), message.getPassword()));
		} else if(message.getType() == 1) {
			return new Message(Message.STATUS, ServerLogin.register(message));
		} else if (message.getType() == 2) {
			ServerMessageHandler.put(message.getRecipient(), message);
			//ska notifiera användaren att nytt medelande finns
		}
		return null;
		
	}
	/**
	 * Method to start logging
	 */
	private void startLog() {
		log = Logger.getLogger("Log");
		try {
			LocalDateTime now = LocalDateTime.now();
			String path = now.getMonthValue()+"-" + now.getDayOfMonth()+"-" + now.getHour()+";" + now.getMinute() +" "; 
			fileHandle = new FileHandler("log/" + path + "log.txt");
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
	protected static void logHandler(String logMessage) {
		log.info(logMessage + "\n");
		serverGUI.append(logMessage);
	}
}
