package serverMain;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * aksdölasmdöasmdöaslkmd MAX ÄR BÄST
 * @author 123
 *
 */
public class serverController implements Runnable {

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

	public static void main(String[] args) throws IOException {
		new serverController(1337);
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
}
