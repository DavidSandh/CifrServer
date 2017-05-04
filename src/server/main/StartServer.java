package server.main;

import java.io.IOException;
/**
 * Starts the server.
 * @author David Sandh, Lucas Knutsäter
 *
 */
public class StartServer {
	public static void main(String[] args) throws IOException {
		ServerGUI serverGUI = new ServerGUI();
		Server server = new Server(1337,serverGUI);
		ServerController serverController = new ServerController(server, serverGUI);	
	}
}
