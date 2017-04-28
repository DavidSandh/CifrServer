package server.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import message.Message;

/**
 * Server that waits for a client to connect.
 * @author Lucas Knutsäter & David Sandh
 *
 */
public class Server implements Runnable {
	private ServerSocket serverSocket;
	private int port;
	private Thread serverThread;
	private ArrayList<ClientHandler> clientList = new ArrayList<ClientHandler>();
	private ServerController serverController;
	private boolean serverStatus;
	
	public Server(int port, ServerGUI viewer) {
		this.port = port;
	}
	
	/**
	 * Method starts the server.
	 */
	protected void startServer() {
		serverStatus = true;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverThread = new Thread(this);
		serverThread.start();
		serverController.logHandler("Server started");
	}
	
	protected void sendNotification(String username) {
		for(int i = 0; i < clientList.size(); i++) {
			if(clientList.get(i).getUsername().equals(username)) {
				Message message = null;
				do{
					message = ServerMessageHandler.remove(username);
					clientList.get(i).writeMessage(message);
				}while(message != null);
			}
		}
	}
	/**
	 * Method which removes a user from list
	 * @param username username to remove
	 * @throws IOException
	 */
	public void removeUser(String username) throws IOException {
		for(int i = 0; i < clientList.size(); i++) {
			if(clientList.get(i).username.equals(username)) {
				clientList.remove(i);
			}
		}
	}
	
	/**
	 * Adds a controller to this class.
	 * @param serverController which controller to add.
	 */
	protected void addController(ServerController serverController) {
		this.serverController = serverController;
	}

	protected void stopServer() {
		try {
			serverStatus = false;
			serverSocket.close();
			serverThread.interrupt();
			serverThread = null;
		} catch (IOException e) {
			serverController.logHandler("Server already closed /n");
		}
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
				clientList.add(newCLient);
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
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private String username = null;
		/**
		 * Controller which opens new streams
		 * @param socket Socket which to open stream on.
		 */
		public ClientHandler(Socket socket) {
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
					message = (Object)input.readObject();
					//Behöver möjligtvis skrivas om.
					if(username == null && ((Message) message).getUsername() != null) {
						username = ((Message) message).getUsername();
					}
					Message messageReturn = serverController.checkType(message);
					if(messageReturn != null) {
						writeMessage(messageReturn);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}finally{
					try {
						removeUser(username);
					} catch (IOException e) {
						e.printStackTrace();
					}
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
		
		protected String getUsername() {
			return username;
		}
	}
}
