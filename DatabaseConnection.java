package BarberShop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private String url = System.getenv("URL_DB");
    private String user = System.getenv("USER_DB");
    private String pass = System.getenv("PASS_DB");

    public Connection connect() throws SQLException {


        Connection conexion = DriverManager.getConnection(url, user, pass);
        return conexion;
    }

    public boolean login(String login,String password){
        return (login.equals(user) && password.equals(pass));

    }


}

