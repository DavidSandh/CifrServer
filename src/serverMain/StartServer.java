package serverMain;

import java.io.IOException;

public class StartServer {
	public static void main(String[] args) throws IOException {
		new Server(1337,new ServerGUI());
	}
}
