package models;

import utils.VariableServer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author casar
 */
public class BDEureka {
    VariableServer vs;

    /**
     *
     */
    protected Connection dbconnection;
   
    /**
     *
     * @return
     */
    public Connection getConnection() {
         
        String host = VariableServer.getHost().trim();//"localhost";"eurekabankaena"
        String port = "3306";
        String dbname = VariableServer.getBaseDatos().trim()+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user =VariableServer.getUser().trim();
        String pass = "";
        if ("1".equals(VariableServer.getPass())) {
             pass="";
        }else{
            pass=VariableServer.getPass();
        }
        String password =pass;
        final String ConnectionString = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        try {
            dbconnection = DriverManager.getConnection(ConnectionString, user, password);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return dbconnection;
    }
  

}
