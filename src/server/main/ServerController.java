package server.main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import message.Message;
import psql.ServerLogin;
import psql.ServerPsqlConnection;
import server.connection.Server;
import server.start.ServerGUI;

/**
 * ServerController
 * @author Lucas Knutsäter & David Sandh
 */
public class ServerController {
	private Server server;
	private static ServerGUI serverGUI;
	private FileHandler fileHandler;
	public static Logger log;

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
	 * Runs method in server that starts the server.
	 */
	public void startServer() {
		server.startServer();
	}

	/**
	 * runs method in server that stops the server.
	 */
	public void stopServer() {
		server.stopServer();
	}

	/**
	 * Check what type object is
	 * 
	 * @param object object to check type on
	 * @return message to return
	 */
	public Message checkType(Object object) {
		Message message = (Message) object;
		ServerPsqlConnection psql = new ServerPsqlConnection();
		int type = message.getType();
		switch (type) {
		case Message.LOGIN :
			return new Message(Message.LOGIN, ServerLogin.loginCheck(message.getUsername(), message.getData()),
					psql.getContactList(message.getUsername()));
		case Message.REGISTER : 
			return new Message(Message.REGISTER, ServerLogin.register(message));
		case Message.MESSAGE :
			ServerMessageHandler.put(message.getRecipient(), message);
			server.sendNotification(message.getRecipient());
		case Message.SEARCH : 
			return new Message(Message.SEARCH, psql.searchUsername(message.getUsername()));
		case Message.CONTACTLIST_ADD :
			psql.insertContactList(message.getUsername(), message.getData());
			return new Message(Message.CONTACTLIST, psql.getContactList(message.getUsername()));
		case Message.CONTACTLIST_REMOVE : 
			psql.removeFromContactList(message.getUsername(), message.getData());
			ServerMessageHandler.put(message.getData(), new Message(Message.CONTACTLIST, psql.getContactList(message.getData())));
			server.sendNotification(message.getData());
			return new Message(Message.CONTACTLIST, psql.getContactList(message.getUsername()));
		}
		return null;
	}

	/**
	 * starts the logger and saves log in log/
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
	 * Logs message
	 * @param logMessage Message to log
	 */
	public static void logHandler(String logMessage) {
		log.info(logMessage + "\n");
		serverGUI.append(logMessage);
	}
}
