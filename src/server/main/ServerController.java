package server.main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import message.Message;

/**
 * ServerController
 * 
 * @author Lucas Knutsäter & David Sandh
 *
 */
public class ServerController {
	private Server server;
	private static ServerGUI serverGUI;
	private FileHandler fileHandler;
	public static Logger log;

	/**
	 * Constructor which adds controller to serverGUI and server
	 * 
	 * @param server
	 *            instance of Server
	 * @param serverGUI
	 *            instance of ServerGUI
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
	 * 
	 * @param object
	 *            object to check
	 * @return message to return
	 */
	protected Message checkType(Object object) {
		Message message = (Message) object;
		ServerPsqlConnection psql = new ServerPsqlConnection();
		if (message.getType() == Message.LOGIN) {
			return new Message(Message.LOGIN, ServerLogin.loginCheck(message.getUsername(), message.getData()),
					psql.getContactList(message.getUsername()));	
		} else if (message.getType() == Message.REGISTER) {
			return new Message(Message.REGISTER, ServerLogin.register(message));
		} else if (message.getType() == Message.MESSAGE) {
			ServerMessageHandler.put(message.getRecipient(), message);
			// ska notifiera användaren att nytt medelande finns
			server.sendNotification(message.getRecipient());
		} else if (message.getType() == Message.SEARCH) {

			return new Message(Message.SEARCH, psql.searchUsername(message.getUsername()));
		} else if (message.getType() == Message.CONTACTLIST_ADD) {

			psql.insertContactList(message.getUsername(), message.getData());
			return new Message(Message.CONTACTLIST, psql.getContactList(message.getUsername()));
		} else if (message.getType() == Message.CONTACTLIST_REMOVE) {

			psql.removeFromContactList(message.getUsername(), message.getData());
			return new Message(Message.CONTACTLIST, psql.getContactList(message.getUsername()));
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
			String path = now.getMonthValue() + "-" + now.getDayOfMonth() + "-" + now.getHour() + ";" + now.getMinute()
					+ " ";
			fileHandler = new FileHandler("log/" + path + "log.txt");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileHandler.setFormatter(new SimpleFormatter());
		log.addHandler(fileHandler);
	}

	/**
	 * Method to add log message
	 * 
	 * @param logMessage
	 *            Message to log
	 */
	protected static void logHandler(String logMessage) {
		log.info(logMessage + "\n");
		serverGUI.append(logMessage);
	}
}
