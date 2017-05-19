package psql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * ServerPsqlConnection connects the server to a psql database that stores usernames, hashed passwords, salts and
 * contact lists.
 * @author Lucas Knutsäter & David Sandh
 *
 */
public class ServerPsqlConnection {
	private Connection connection;
	private Statement statement = null;
	// JDBC driver name and database URL
	   static final String JDBC_DRIVER = "or.postgres.Driver";  
	   static final String DB_URL = "jdbc:postgresql://pgserver.mah.se/cifr2";
	   //Database credentials
	   static final String USER = "ag7713";
	   static final String PASS = "5mf10n4r";
	   
	/**
	 * Connects the server to the psql-database.
	 */
	public void connect(){
		try{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			connection.setAutoCommit(false);
		}catch(Exception e){
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}
	}
	/**
	 * Takes a username, password and salt as arguments and inserts these into the psql-database.
	 * @param username
	 * @param password
	 * @param salt
	 */
	public void insertRegister(String username, String password, String salt){
		connect();
		try {
			statement = connection.createStatement();
			String command = "INSERT INTO users (username, password) VALUES ('" + username.toLowerCase() +"', '" + password + "')";
			statement.executeUpdate(command);
			command = "INSERT INTO salt (username, salt) VALUES ('" + username.toLowerCase() +"', '" + salt + "')";
			statement.executeUpdate(command);
	         connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
	}
	/**
	 * Takes a String and attempts to select it from the database. If no such string exists it returns true, else it returns false.
	 * @param username
	 * @return true if there is no matching string in the database, else false.
	 */
	public boolean checkIfAvailable(String username){
		connect();
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT username FROM users WHERE username = '" + username.toLowerCase() +"';");
			while(rs.next()){
				String checkUsername = rs.getString("username");
				if(checkUsername.equals(username.toLowerCase())) {
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
	/**
	 * Takes a username as an argument and attempts to select the hash-value connected to this username.
	 * @param username
	 * @return password
	 */
	public String selectPassword(String username){
		String password = null;
		connect();
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT password FROM users WHERE username = '" + username.toLowerCase() + "';");
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
	/**
	 * Takes a username as an argument and attempts to select the salt used for producing a hash-value.
	 * @param username
	 * @return salt
	 */
	public String selectSalt(String username){
		String salt = null;
		connect();
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT salt FROM salt WHERE username = '" + username.toLowerCase() + "';");
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
	/**
	 * Takes a username as an argument and attempts to select it from the database.
	 * If no such username exists it returns null.
	 * @param username
	 * @return null if no username exists, else username
	 */
	public String searchUsername(String username) {
		connect();
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT username FROM users WHERE username = '"+username.toLowerCase() + "';");
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
	/**
	 * Takes a username and a userToAdd and inserts these as a set of values in the database.
	 * @param username
	 * @param userToAdd
	 */
	public void insertContactList(String username, String userToAdd) {
		connect();
		try{
			statement = connection.createStatement();
			String command = "INSERT INTO kontaktlista (user1, user2) VALUES ('" + username.toLowerCase() +"', '" + userToAdd.toLowerCase() + "')";
			statement.executeUpdate(command);
	        connection.commit();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			close();
		}
	}
	/**
	 * Takes a username and a userToAdd as arguments and removes this dataset from the database.
	 * @param username
	 * @param userToRemove
	 */
	public void removeFromContactList(String username, String userToRemove){
		connect();
		try{
			statement = connection.createStatement();
			String command = "DELETE FROM kontaktlista WHERE user1 ='" + username +"' AND user2 = '" + userToRemove +"';";
			statement.executeUpdate(command);
			connection.commit();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			close();
		}
	}
	/**
	 * Takes a username as an argument and returns all corresponding users from the contactlist in the database. 
	 * If there is none, returns null
	 * @param username
	 * @return String[] with usernames if these exist, else null.
	 */
	public String[] getContactList(String username) {
		connect();
		String[] result = null;
		try{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String command = "SELECT user2 FROM kontaktlista WHERE user1 = '"+username.toLowerCase() +"';";
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
	/**
	 * Closes all statements and connections to the database.
	 */
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
