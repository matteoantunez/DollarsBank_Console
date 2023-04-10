package com.dollarsbank.application;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dollarsbank.connection.SQLConnection;
import com.dollarsbank.model.Customer;
import com.dollarsbank.utility.ConsolePrinterUtility;

public class DollarsBanksApplication {
	

	public static void main(String[] args) {
		// Variables / User
		ArrayList<Customer> users = new ArrayList<>();
		Customer currentUser;
		
		Connection conn = SQLConnection.getConnection();
		
		// Create Objects
		ConsolePrinterUtility print = new ConsolePrinterUtility();
		
		// Print welcome screen
		int response = print.welcome();
		
		while(response != 3) {
			switch(response) {
				case 1:
					users.add(print.createAccount(conn));
					break;
				case 2:
					currentUser = print.login( users, conn);
					print.menu( currentUser, conn);
					break;
				case 3:
					System.out.println("Goodbye!");
					break;
			}
			
			response = print.welcome();
		}
		
		// Closes connection, should never run
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to close connection.");
		}
		
	}

}
