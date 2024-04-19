package com.johnnyconsole.sis.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://johnnyconsole.com:3306/johnnyco_sis?serverTimezone=America/Toronto",
                    "johnnyco_sis",
                    "Marbles1999"
            );
        }
        catch (Exception e) {
            return null;
        }
    }
}
