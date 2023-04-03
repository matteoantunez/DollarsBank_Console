package com.dollarsbank.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	
	// Recent updates have made this line obsolete on Mac & Linux, So your mileage may vary
	// ?serverTimezon=EST5EDT
	private static final String URL = "jdbc:mysql://localhost:3306/university";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "Root@123";
	
	public static Connection getConnection() {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("Connected.");
		} catch (SQLException e) {
			System.out.println("Could not make a connection.");
		}
		
		return conn;
	}
	
	public static void main (String[] args) {
		Connection conn = ConnectionManager.getConnection();
		
		try {
			conn.close();
			System.out.println("Connection close.");
		} catch (SQLException e) {
			System.out.println("Connection could not be closed.");
		}
	}
	
}
