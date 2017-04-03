package serverMain;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerController {
	private Server server;
	private ServerGUI serverGUI;
	private Logger log;
	private FileHandler fileHandle;
	
	public ServerController(Server server, ServerGUI serverGUI) {
		this.server = server;
		this.serverGUI = serverGUI;
		serverGUI.addController(this);
		server.addController(this);
	}
	
	public void startServer() {
		server.startServer();
	}
	
	public void stopServer() {
		server.stopServer();
	}
	
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

	public void logHandler(String logMessage) {
		log.info(logMessage + "\n");
	}
}
