package serverMain;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class serverController implements Runnable {
	private Logger log;
	private FileHandler fileHandle;
	private ServerSocket serverSocket;
	private Thread server = new Thread(this);
	private ArrayList<ClientHandler> list = new ArrayList<ClientHandler>();

	public serverController(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		server.start();
	}
	public void sendMessage(String message){
		for(int i = 0; i<list.size();i++){
			ClientHandler sendTo = list.get(i);
			sendTo.writeMessage(message);
		}
	}

	@Override
	public void run() {
		 try{
			 while(true){
				 Socket socket = serverSocket.accept();
				 ClientHandler newCLient = new ClientHandler(socket);
				 newCLient.start();
				 list.add(newCLient);
				 
			 }
				 
			 }catch(Exception e){
				 
			 }
	}

	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectOutputStream output;
		private ObjectInputStream input;

		public ClientHandler(Socket socket) {
			this.socket = socket;
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
				output.flush();

			} catch (IOException ioe) {

			}

		}

		public void run() {
			String message;
			while(true){
				try {
			
				message = (String)input.readObject();
				sendMessage(message);
				
			} catch (ClassNotFoundException e) {
				 
				e.printStackTrace();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}}
		private void writeMessage(String message){
			try {
				output.writeObject(message);
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
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
	
	private void logHandler(String logMessage) {
		log.info(logMessage + "\n");
	}
}
