package tema2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler {
	private Connection conn = null;
	private String url = "jdbc:mysql://localhost:3306/sql_local001";
	private String user= "root";
	private String password ="root";

	public Connection doConnection(){                     
            try {
                Class.forName("com.mysql.jdbc.Driver");            
                conn = DriverManager.getConnection(url,user,password);
                if (conn!=null){
                        System.out.println("Connected to "+conn.toString() );
                }              
                return conn;
            } catch (SQLException e) {
                System.out.println("Wrong sql_url, sql_username or sql_password");
                //if (e.Number == 18456) { // invalid login
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                //Logger.getLogger(MysqlConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                e.printStackTrace();
                return null;
            } 
	}
	
	public void closeConnection(){
		try {
			conn.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Close connection error");
		}
	}	
	
}
