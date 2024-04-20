package com.johnnyconsole.sis.session;

import com.johnnyconsole.sis.database.Database;

import java.sql.Connection;

public class Session {

    public static Connection connection;
    public static String username, last, first, type, program, status;
    public static int studentNumber;


    public static void connect() {
        if(connection == null)
            connection = Database.connect();
    }

    public static void disconnect() {
        connection = null;
    }

    public static void clear() {
        username = last = first = type = program = status = null;
        studentNumber = 0;
    }

}
