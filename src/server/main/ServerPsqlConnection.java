package server.main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerPsqlConnection {
	private Connection connection;
	private Statement statement = null;
	// JDBC driver name and database URL
	   static final String JDBC_DRIVER = "or.postgres.Driver";  
	   static final String DB_URL = "jdbc:postgresql://pgserver.mah.se/cifr2";

	   //  Database credentials
	   static final String USER = "ag7713";
	   static final String PASS = "5mf10n4r";
	   
	
	public void connect(){
		try{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			connection.setAutoCommit(false);
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}
		 System.out.println("Opened database successfully");
		 
	}
	public void insert(String username, String password, String salt){
		try {
			
			statement = connection.createStatement();
			String command = "insert into users (username, password) values ('" + username +"', '" + password + "')";
			statement.executeUpdate(command);
			System.out.println(command);
			command = "insert into salt (username, salt) values ('" + username +"', '" + salt + "')";
			statement.executeUpdate(command);
			System.out.println(command);
			 statement.close();
	         connection.commit();
	         connection.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean checkIfAvailable(String username){
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select username from users where username = '" + username +"';");
			while(rs.next()){
			String name = rs.getString("username");
			System.out.println(name);
			rs.close();
			statement.close();
			connection.close();
			}
		}catch(Exception e){
			
		}
		
		return true;
	}
	public void select(String username){
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select password from users where username = " +username + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args){
		ServerPsqlConnection spc = new ServerPsqlConnection();
		spc.connect();
//		spc.insert("david", "lösenord", "saltsten");
		spc.checkIfAvailable("david");
	}
}