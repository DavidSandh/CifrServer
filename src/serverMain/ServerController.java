package serverMain;

public class ServerController {
	private Server server;
	private ServerGUI serverGUI;
	
	public ServerController(Server server, ServerGUI serverGUI) {
		this.server = server;
		this.serverGUI = serverGUI;
		serverGUI.addController(this);
	}
	
	public void startServer() {
		server.startServer();
	}
	
	public void stopServer() {
		
	}

}
