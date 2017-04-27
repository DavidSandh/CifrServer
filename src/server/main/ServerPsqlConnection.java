package server.main;
import java.sql.Array;
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
	   //Database credentials
	   static final String USER = "ag7713";
	   static final String PASS = "5mf10n4r";
	   
	
	public void connect(){
		try{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			connection.setAutoCommit(false);
		}catch(Exception e){
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}
	}
	
	public void insertRegister(String username, String password, String salt){
		connect();
		try {
			statement = connection.createStatement();
			String command = "insert into users (username, password) values ('" + username.toLowerCase() +"', '" + password + "')";
			statement.executeUpdate(command);
			System.out.println(command);
			command = "insert into salt (username, salt) values ('" + username.toLowerCase() +"', '" + salt + "')";
			statement.executeUpdate(command);
			System.out.println(command);
	         connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
	}
	
	public boolean checkIfAvailable(String username){
		String name = username.toLowerCase();
		connect();
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select username from users where username = '" + name +"';");
			while(rs.next()){
				String checkUsername = rs.getString("username");
				if(checkUsername.equals(username)) {
					System.out.println(checkUsername);
					close();
					return false;
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			close();
		}
		return false;
	}
	
	public String selectPassword(String username){
		String password = null;
		connect();
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select password from users where username = '" + username + "';");
			while(rs.next()){
				password = rs.getString("password");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return password;
	}
	
	public String selectSalt(String username){
		String salt = null;
		connect();
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select salt from salt where username = '" + username + "';");
			while(rs.next()){
				salt = rs.getString("salt");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return salt;
	}
	
	public String searchUsername(String username) {
		connect();
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select username from users where username = '"+username + "';");
			while(rs.next()){
				return rs.getString("username");
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return null;
	}
	
	public void insertContatList(String username, String userToAdd) {
		connect();
		try{
			statement = connection.createStatement();
			String command = "insert into kontaktlista (user1, user2) values ('" + username.toLowerCase() +"', '" + userToAdd.toLowerCase() + "')";
			statement.executeUpdate(command);
	        connection.commit();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			close();
		}
	}
	
	public String[] getContactList(String username) {
		connect();
		String[] result = null;
		try{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String command = "select user2 from kontaktlista where user1 = '"+username.toLowerCase() +"';";
			ResultSet rs = statement.executeQuery(command);
			if(rs.last()) {
				result = new String[rs.getRow()];
				rs.beforeFirst();
				int i = 0;
				while(rs.next()) {
					result[i] = rs.getString("user2");
					i++;
				}
			}
			return result;
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}
	
	private void close() {
		try {
			if(!statement.isClosed()) {
				statement.close();
			}
			if(!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
