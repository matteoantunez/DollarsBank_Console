package com.dollarsbank.application;

import java.util.ArrayList;

import com.dollarsbank.model.Customer;
import com.dollarsbank.utility.ConsolePrinterUtility;

public class DollarsBanksApplication {
	

	public static void main(String[] args) {
		// Variables / User
		ArrayList<Customer> users = new ArrayList<>();
		Customer currentUser;
		
		// Create Objects
		ConsolePrinterUtility print = new ConsolePrinterUtility();
		
		// Print welcome screen
		int response = print.welcome();
		
		while(response != 3) {
			switch(response) {
				case 1:
					users.add(print.createAccount());
					break;
				case 2:
					currentUser = print.login( users);
					print.menu( currentUser);
					break;
				case 3:
					System.out.println("Goodbye!");
					break;
			}
			
			response = print.welcome();
		}
		
		
	}

}
