package server.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import message.Message;
import server.main.ServerController;
import server.main.ServerMessageHandler;
import server.start.ServerGUI;

/**
 * Server that handles client connections.
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
	private static int uniqueId;
	
	public Server(int port, ServerGUI viewer) {
		this.port = port;
	}
	
	/**
	 * makes a serverSocket on port and starts server.
	 */
	public void startServer() {
		serverStatus = true;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverThread = new Thread(this);
		serverThread.start();
		ServerController.logHandler("Server started");
	}
	public synchronized ClientHandler getClientList(int index) {
		return clientList.get(index);
	}
	public synchronized void removeClientList(int index) {
		clientList.remove(index);
	}
	/**
	 * Checks if username is in clientlist. If it is sends all messages in ServerMessegeHandler to the user.
	 * @param username username to send messages to.
	 */
	public void sendNotification(String username) {
		for(int i = 0; i < clientList.size(); i++) {
			String user = clientList.get(i).username;
			if(user != null && username.equals(user)) {
				Message message = ServerMessageHandler.remove(username);
				while(message != null) {
					getClientList(i).writeMessage(message);
					ServerController.logHandler("Sent message to " + username);
					message = ServerMessageHandler.remove(username);
				}
			}
		}
	}
	/**
	 * removes username from clientlist.
	 * @param id, String username username to remove
	 * @throws IOException
	 */
	public void removeUser(int id, String username) throws IOException {
		for(int i = 0; i < clientList.size(); i++) {
			if(getClientList(i).id == id) {
				removeClientList(i);
				ServerController.logHandler(username + " disconnected");
			}
		}
	}
	
	/**
	 * Adds a controller to this class.
	 * @param serverController controller to add
	 */
	public void addController(ServerController serverController) {
		this.serverController = serverController;
	}
	/**
	 * Stops the server.
	 * Closes the socket and makes serverThread null.
	 */
	public void stopServer() {
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
	 * Class handles individual clients
	 * @author Lucas Knutsäter & David Sandh
	 *
	 */
	private class ClientHandler extends Thread {
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private String username;
		int id;
		/**
		 * Controller which opens new streams
		 * @param socket Socket to open stream on.
		 */
		public ClientHandler(Socket socket) {
			id = ++uniqueId;
			username = Integer.toString(id);
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
				output.flush();
			} catch (IOException ioe) {

			}
		}
		/**
		 * Run method which waits for input from client. Then sends the recieved object to serverController.checkType
		 * 
		 */
		public void run() {
			Object message;
			while (!Thread.interrupted()) {
				try {
					message = (Object)input.readObject();
					if(username.equals(Integer.toString(id)) && ((Message) message).getUsername() != null) {
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
					try {
						removeUser(id, username);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				}
			}
		}
		
		/**
		 * writeObject to client with message
		 * @param message message to send to client
		 */
		private void writeMessage(Message message) {
			try {
				output.writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/**
		 * get username
		 * @return username returns username of client
		 */
		protected String getUsername() {
			return username;
		}
	}
}
