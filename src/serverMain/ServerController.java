package serverMain;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 * ServerController
 * @author Lucas
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
	}
}
