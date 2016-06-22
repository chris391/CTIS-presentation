package SourceCode.BusinessLogic;

import SourceCode.Controller.main.MainViewController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.sql.*;

public class ConnectDB {
    public static Connection conn;

    /* CONSTRUCTOR */
    public ConnectDB() throws Exception{
        connectToDatabase();
    }

    /* DATABASE CONNECTION */
    private static void connectToDatabase() throws Exception{
        System.out.println("***********Welcome to Racoon**************");
        try {
//            String DB_URL = "jdbc:mysql://ctis-racoon.c7zmk0iubeje.eu-central-1.rds.amazonaws.com:3306/ctisracoon";
//            String DB_URL = "jdbc:mysql://ctis-racoon.c7zmk0iubeje.eu-central-1.rds.amazonaws.com:3306/ctisracoon"+
//                    "?verifyServerCertificate=false"+
//                    "&useSSL=true"+
//                    "&requireSSL=true";
//            String USER = "aime3444";
//            String PASS = "CTISracoon84";

            String DB_URL = "jdbc:mysql://localhost:3306/Test";
            String USER = "root";
            String PASS = "Cristian344";
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("conn obj created " + conn + " message: ");
        } catch (SQLException e) {
            MainViewController.updateWarningMessage("The application couldn't connect to the database");
            System.out.println("DB error" + e.getMessage());
        }
    }

    /* RESULT SET METHOD */
    public ResultSet resultSet(String sql) {
        try {
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println("Error in resultSet() from ConnectDB class");
            return null;
        }
    }

    /* PREPARED STATEMENT METHOD */
    public PreparedStatement preparedStatement(String sql) {
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException ex) {
            System.out.println("Error in preparedStatement() from ConnectDB class");
            return null;
        }
    }


}
