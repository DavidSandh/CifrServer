package serverMain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
/**
 * Server that waits for a client to connect.
 * @author Lucas Knutsäter & David Sandh
 *
 */
public class Server implements Runnable {
	private ServerSocket serverSocket;
	private int port;
	private Thread serverThread = new Thread(this);
	private ArrayList<ClientHandler> list = new ArrayList<ClientHandler>();
	private ServerGUI serverGUI;
	private ServerController serverController;
	private boolean serverStatus;
	
	public Server(int port, ServerGUI viewer) {
		this.port = port;
		this.serverGUI = serverGUI;
	}
	/**
	 * Method starts the server.
	 */
	public void startServer() {
		serverStatus = true;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverController.logHandler("Server started");
		serverThread.start();
	}
	/**
	 * Adds a controller to this class.
	 * @param serverController which controller to add.
	 */
	public void addController(ServerController serverController) {
		this.serverController = serverController;
	}

	public void stopServer() {
		try {
			serverStatus = false;
			serverSocket.close();
		} catch (IOException e) {
			serverController.logHandler("Server already closed /n");
		}
	}
	/**
	 * method for demo only. Placeholder, Will be removed/remade.
	 * @param message
	 */
	public void sendMessage(String message) {
		
//		for (int i = 0; i < list.size(); i++) {
//			ClientHandler sendTo = list.get(i);
//			sendTo.writeMessage(message);
//		}
	}
	/**
	 *  Run method which waits for a serverSocket to accept. Adds the client to a list
	 *  and starts a thread to handle the client.
	 */
	@Override
	public void run() {
		try {
			while (serverStatus) {
				Socket socket = serverSocket.accept();
				ClientHandler newCLient = new ClientHandler(socket);
				newCLient.start();
				list.add(newCLient);
			}
		} catch (Exception e) {

		}
	}
	/**
	 * inner class which handles clients.
	 * @author Lucas Knutsäter & David Sandh
	 *
	 */
	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectOutputStream output;
		private ObjectInputStream input;
		/**
		 * Controller which opens new streams
		 * @param socket Socket which to open stream on.
		 */
		public ClientHandler(Socket socket) {
			this.socket = socket;
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
				output.flush();

			} catch (IOException ioe) {

			}

		}
		/**
		 * Placeholder method. Was used for demo. Will be removed/remade.
		 */
		public void run() {
			Object message;
			while (!Thread.interrupted()) {
				try {
					message = input.readObject();
					Message messageReturn = serverController.checkType(message);
					if(messageReturn != null) {
						writeMessage(messageReturn);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					break;
					
				} 
			}
		}
		/**
		 * Placeholder method. Was used for demo. Will b e removed/remade.
		 * @param message
		 */
		private void writeMessage(Message message) {
			try {
				output.writeObject(message);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
}
