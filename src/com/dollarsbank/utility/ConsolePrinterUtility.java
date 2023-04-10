package com.dollarsbank.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import com.dollarsbank.connection.SQLConnection;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.SavingsAccount;

public class ConsolePrinterUtility {
	// Create Scanner
	private Scanner scan = new Scanner(System.in);

	// Create Universal color utility of the printer
	private ColorsUtility colors = new ColorsUtility();
	
	
	// Prints the welcome and asks for prompt
	public int welcome() {
				
		//  Prints out welcome
		System.out.println(colors.getAnsiBlue() + "+---------------------------+" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "| DOLLARSBANK Welcomes You! |" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "+---------------------------+" + colors.getAnsiReset());
		
		// List possible options
		System.out.println("1. Create New Account");
		System.out.println("2. Login");
		System.out.println("3. Exit.");
		
		// Set up while loop to ensure input is grabbed
		// Prompt for input
		System.out.print("\n" + colors.getAnsiGreen()  + "Enter Choice (1,2 or 3): " + colors.getAnsiReset());
		while(true) {
			try {
				String response = scan.nextLine();
				int answer = Integer.parseInt(response);
				if (answer == 3) {
					scan.close();
				}
				System.out.println();
				return answer;
			} catch (Exception e) {
				System.out.println("Response not valid, please try again");
			}
		}
		
	}

	// Prints and collects the New Account information and should return the account and user
	public Customer createAccount(Connection conn) {
		
		//  Prints out Header
		System.out.println(colors.getAnsiBlue() + "+-------------------------------+" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "| Enter Details For New Account |" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "+-------------------------------+" + colors.getAnsiReset());

		// Collect name and split
		System.out.print(colors.getAnsiGreen() + "Customer Name (i.e. John Doe): " + colors.getAnsiReset());
		String name = scan.nextLine();
		String[] firstLast = name.split(" ", 2);
		
		// Grab customer State
		System.out.print(colors.getAnsiGreen() + "Customer State (i.e. NE): " + colors.getAnsiReset());
		String state = scan.nextLine();
		
		// Grab customer number
		System.out.print(colors.getAnsiGreen() + "Customer Phone Number (i.e. 111-111-1111): " + colors.getAnsiReset());
		String phone = scan.nextLine();
		String[] phoneSplit = phone.split("-");
		
		// Convert String[] to int[]
		int[] phoneNumber = new int[3];
		for(int x = 0; x < phoneSplit.length; x++) {
			int number = Integer.parseInt(phoneSplit[x]);
			phoneNumber[x] = number;
		}
		
		// Grab username
		System.out.print(colors.getAnsiGreen() + "Username (i.e. JDoe23): " + colors.getAnsiReset());
		String username = scan.nextLine();
		
		// Grab password
		System.out.print(colors.getAnsiGreen() + "Password (i.e. johnDoe23!): " + colors.getAnsiReset()); //(8 Characters W/ Lower, Upper & Special)
		String password = scan.nextLine();
		
		// Grab Initial Deposit
		System.out.print(colors.getAnsiGreen() + "Inital Deposit Ammount: $" + colors.getAnsiReset());
		String input = scan.nextLine();
		float deposit = Float.parseFloat(input);
		
		// Initiate Queue for transactions
		Queue<String> transactions = new LinkedList<>();
		String trans = "Initial Deposit Amount in account " + username + "\nBalance - $" + deposit + " as of " + LocalDate.now();
		transactions.add(trans);
		
		// Create Customer & Account -- Persistence
		Customer customer = new Customer(firstLast[0], firstLast[1], username, password, state, phoneNumber[0], phoneNumber[1],  phoneNumber[2]);
		Account account = new Account(deposit, transactions, customer);
		SavingsAccount sAccount = new SavingsAccount(customer);
		customer.setChecking(account);
		customer.setSavings(sAccount);
		
		// Insert data into database to create User
		try{
		
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO CUSTOMERS (first_name, last_name, username, psword, state, area_code, local_code, personal_digits) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, firstLast[0]);
			stmt.setString(2, firstLast[1]);
			stmt.setString(3, username);
			stmt.setString(4, password);
			stmt.setString(5, state);
			stmt.setInt(6, phoneNumber[0]);
			stmt.setInt(7, phoneNumber[1]);
			stmt.setInt(8, phoneNumber[2]);
			
			stmt.execute();
			
			PreparedStatement gstmt = conn.prepareStatement("Select customer_id from CUSTOMERS where username = ?");
			gstmt.setString(1, username);
			ResultSet rs = gstmt.executeQuery();
			
			int id = 0;
			
			while(rs.next()) {
				id = rs.getInt(1);			
			}
			
			stmt = conn.prepareStatement("INSERT INTO Checking_Account (customer_id, balance) VALUES (?, ?)");
			stmt.setInt(1, id);
			stmt.setFloat(2, deposit);
			stmt.execute();
			
			gstmt = conn.prepareStatement("Select account_id from Checking_Account WHERE customer_id = ?");
			gstmt.setInt(1, id);
			rs = gstmt.executeQuery();
			
			while(rs.next()) {
				id = rs.getInt(1);
			}
			
			stmt = conn.prepareStatement("INSERT INTO Transactions_Checking (account_id, transaction_description, debit, credit) Values (1, ?, ?, NULL, ?");
			stmt.setInt(1, id);
			stmt.setString(2, trans);
			stmt.setFloat(3, deposit);
			
		} catch (SQLException e) {
			System.out.println("Username already taken, please try again.");
		}
		
		
		
		
		System.out.println();
		// Return customer
		return customer;
	}
	
	
	// Resume Here
	public Customer login( ArrayList<Customer> customers, Connection conn) {
		//  Prints out Header
		System.out.println(colors.getAnsiBlue() + "+---------------------+" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "| Enter Login Details |" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "+---------------------+" + colors.getAnsiReset());
		
		// Continuously ask for log in credentials until correct
		while(true) {
			// Grab login
			System.out.print(colors.getAnsiGreen() + "Username: " + colors.getAnsiReset());
			String username = scan.nextLine();
			
			System.out.print(colors.getAnsiGreen() + "Password: " + colors.getAnsiReset());
			String password = scan.nextLine();
			
			System.out.println();
			
			try {
				PreparedStatement gstmt = conn.prepareStatement("Select customer_id from CUSTOMERS where username = ? and psword = ?");
				gstmt.setString(1, username);
				gstmt.setString(2, password);
				ResultSet rs = gstmt.executeQuery();
				
				int id = 0;
				
				while(rs.next()) {
					id = rs.getInt(1);			
				}
			} catch (SQLException e) {
				System.out.println("Customer not in database, please try again.");
				e.printStackTrace();
				
			}
			
		
			for (int x  = 0; x < customers.size(); x++) {
				Customer cust = customers.get(x);
				if (cust.getUsername().contentEquals(username) && cust.getPassword().contentEquals(password)){
					return cust;
				}
			}
			
			
			System.out.println(colors.getAnsiRed() + "Invalid Input or Login Credentials. Please try Again\n" + colors.getAnsiReset());
		}
	}
	
	public void menu( Customer cus, Connection conn) {
		//  Loop until logged out
		int res = 1;
		while (res != 6) {
			//  Prints out Header
			System.out.println(colors.getAnsiBlue() + "+-------------------+" + colors.getAnsiReset());
			System.out.println(colors.getAnsiBlue() + "| WELCOME Customer! |" + colors.getAnsiReset()); 
			System.out.println(colors.getAnsiBlue() + "+-------------------+" + colors.getAnsiReset());
			
			
			// Print Current Balances
			System.out.println(colors.getAnsiCyan() + "Checking Balance: $" + cus.getChecking().getBalance() + " | Savings Balance: $" + cus.getSavings().getBalance() + colors.getAnsiReset());
			// Prints out options
			System.out.println("1. Deposit Amount");
			System.out.println("2. Withdraw Amount");
			System.out.println("3. Fund Transfer");
			System.out.println("4. View 5 Recent Transactions");
			System.out.println("5. Display Customer Information");
			System.out.println("6. Sign Out");
			
			System.out.println();
			// Print prompt and gather response
			System.out.print(colors.getAnsiGreen() + "Enter Choice (1,2,3,4,5 or 6): " + colors.getAnsiReset());
			String response = scan.nextLine();
			System.out.println();
			
			try {
				res = Integer.parseInt(response);
			} catch (Exception e) {
				res = 0;
			}
			// Menu Options
			switch (res) {
				case 1:
					cus = deposit(cus, conn);
					break;
				case 2:
					cus = withdraw(cus, conn);
					break;
				case 3: 
					cus = transfer(cus, conn);
					break;
				case 4:
					recent(cus, conn);
					break;
				case 5:
					info(cus);
					break;
				case 6:
					break;
				default: 
					System.out.println(colors.getAnsiRed() + "Response not accepted, please try again." + colors.getAnsiReset());
					break;
					
				}
		
		}
	}
	

	public Customer deposit(Customer cus, Connection conn) {
		// Print out prompt for deposit
		System.out.println(colors.getAnsiBlue() + "+--------------+" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "| DEPOSIT Menu |" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "+--------------+" + colors.getAnsiReset());
		
		// Grab deposit amount
		System.out.print(colors.getAnsiGreen() + "What amount would you like to deposit?: $" + colors.getAnsiReset());
		String response = scan.nextLine();
		float deposit = Float.parseFloat(response);
		
		// Update Deposit
		cus.getChecking().setBalance(cus.getChecking().getBalance() + deposit);
		
		try {
			PreparedStatement gstmt = conn.prepareStatement("Select customer_id from CUSTOMERS where username = ? and psword = ?");
			gstmt.setString(1, cus.getUsername());
			gstmt.setString(2, cus.getPassword());
			ResultSet rs = gstmt.executeQuery();
			
			int id = 0;
			
			while(rs.next()) {
				id = rs.getInt(1);			
			}
			
			gstmt = conn.prepareStatement("Update Checking_Account SET balance = ? where customer_id = ?");
			gstmt.setFloat(1, cus.getChecking().getBalance());
			gstmt.setInt(2, id);
			
		} catch (SQLException e) {
			System.out.println("Error Updating Checking Balance, please try again.");
		}
		
		
		// Add deposit to Account
		cus.getChecking().getTransactions().add("Deposit of " + deposit  + " for " + cus.getUsername() + "\nBalance - $" + cus.getChecking().getBalance() + " as of " + LocalDate.now() );
		
		
		try {
			PreparedStatement gstmt = conn.prepareStatement("Select account_id from CUSTOMERS, Checking_Account where username = ? and psword = ?");
			gstmt.setString(1, cus.getUsername());
			gstmt.setString(2, cus.getPassword());
			ResultSet rs = gstmt.executeQuery();
			
			int id = 0;
			
			while(rs.next()) {
				id = rs.getInt(1);
			}
			
			gstmt = conn.prepareStatement("Update Transactions_Checking SET transaction_description = ?, credit = ? where account_id = ?");
			gstmt.setString(1, "Deposit of " + deposit  + " for " + cus.getUsername() + "\nBalance - $" + cus.getChecking().getBalance() + " as of " + LocalDate.now() );
			gstmt.setFloat(2, deposit);
			gstmt.setInt(3, id);
			
			
		} catch (SQLException e) {
			System.out.println("Error Updating Checking_Transaction, please try again.");
		}
		
		return cus;
	}
	
	public Customer withdraw(Customer cus, Connection conn) {
		// Print out prompt for withdraw
		System.out.println(colors.getAnsiBlue() + "+---------------+" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "| WITHDRAW Menu |" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "+---------------+" + colors.getAnsiReset());
		
		// Grab withdraw amount
		System.out.print(colors.getAnsiGreen() + "What amount would you like to withdraw?" + colors.getAnsiReset());
		String response = scan.nextLine();
		float withdraw = Float.parseFloat(response);
		
		// Update Withdraw
		cus.getChecking().setBalance(cus.getChecking().getBalance() - withdraw);
		try {
			PreparedStatement gstmt = conn.prepareStatement("Select customer_id from CUSTOMERS where username = ? and psword = ?");
			gstmt.setString(1, cus.getUsername());
			gstmt.setString(2, cus.getPassword());
			ResultSet rs = gstmt.executeQuery();
			
			int id = 0;
			
			while(rs.next()) {
				id = rs.getInt(1);			
			}
			
			gstmt = conn.prepareStatement("Update Checking_Account SET balance = ? where customer_id = ?");
			gstmt.setFloat(1, cus.getChecking().getBalance());
			gstmt.setInt(2, id);
			
		} catch (SQLException e) {
			System.out.println("Error Updating Checking Balance, please try again.");
		}
		
		
		// Add withdraw to Account
		cus.getChecking().getTransactions().add("Withdraw of " + withdraw  + " for " + cus.getUsername() + "\nBalance - $" + cus.getChecking().getBalance() + " as of " + LocalDate.now() );
		try {
			PreparedStatement gstmt = conn.prepareStatement("Select account_id from CUSTOMERS, Checking_Account where username = ? and psword = ?");
			gstmt.setString(1, cus.getUsername());
			gstmt.setString(2, cus.getPassword());
			ResultSet rs = gstmt.executeQuery();
			
			int id = 0;
			
			while(rs.next()) {
				id = rs.getInt(1);
			}
			
			gstmt = conn.prepareStatement("Update Transactions_Checking SET transaction_description = ?, debit = ? where account_id = ?");
			gstmt.setString(1, "Withdraw of " + withdraw  + " for " + cus.getUsername() + "\nBalance - $" + cus.getChecking().getBalance() + " as of " + LocalDate.now() );
			gstmt.setFloat(2, withdraw);
			gstmt.setInt(3, id);
			
			
		} catch (SQLException e) {
			System.out.println("Error Updating Checking_Transaction, please try again.");
		}
		
		return cus;
	}
	
	public Customer transfer(Customer cus, Connection conn) {
		// Print out prompt for transfer
		System.out.println(colors.getAnsiBlue() + "+---------------+" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "| TRANSFER Menu |" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "+---------------+" + colors.getAnsiReset());
		
		// Grab Transfer Amount
		while (true) {
			// Prompt for transfer amount
			System.out.print("How much would you like to transfer?\n$");
			String input = scan.nextLine();
			System.out.println();
			float transfer = Float.parseFloat(input);
			
			// Where funds are now
			System.out.println("From Where:\n1.) Checking\n2.) Savings");
			System.out.print(colors.getAnsiGreen() + "Choice: " + colors.getAnsiReset());
			input = scan.nextLine();
			int from = Integer.parseInt(input);
			
			// Grab current values of accounts
			float checking = cus.getChecking().getBalance();
			float savings = cus.getSavings().getBalance();
			
			// Transfer from checking to savings
			if (from == 1) {
				try {
					//  Validate there is enough in the account
					if (checking < transfer) {
						throw new Exception();
					}
					
					// Complete transfer
					checking -= transfer;
					savings += transfer;
					
					// Update Customer
					cus.getChecking().setBalance(checking);
					cus.getChecking().getTransactions().add("Transfer of " + transfer  + " for " + cus.getUsername() + " to Savings\nBalance - " + cus.getChecking().getBalance() + " as of " + LocalDate.now() );
					cus.getSavings().setBalance(savings);
					cus.getSavings().getTransactions().add("Transfer of " + transfer  + " for " + cus.getUsername() + " from Checking\nBalance - " + cus.getSavings().getBalance() + " as of " + LocalDate.now() );
					
					// Repsponse to console
					System.out.println(colors.getAnsiGreen() + "Transfer Completed." + colors.getAnsiReset());
					System.out.println();
				} catch (Exception e) {
					System.out.println(colors.getAnsiRed() + "NSF - Please Try Again" +  colors.getAnsiReset());
					System.out.println();
				}
				
				return cus;
			} else if (from == 2) {
				try {
					//  Validate there is enough in the account
					if (savings < transfer) {
						throw new Exception();
					}
					
					// Complete transfer
					checking += transfer;
					savings -= transfer;
					
					// Update Customer
					cus.getChecking().setBalance(checking);
					cus.getChecking().getTransactions().add("Transfer of" + transfer  + " for " + cus.getUsername() + " from Savings\nBalance - " + cus.getChecking().getBalance() + " as of " + LocalDate.now() );
					cus.getSavings().setBalance(savings);
					cus.getSavings().getTransactions().add("Transfer of" + transfer  + " for " + cus.getUsername() + " to Checking\nBalance - " + cus.getSavings().getBalance() + " as of " + LocalDate.now() );
				
					// Repsponse to console
					System.out.println(colors.getAnsiGreen() + "Transfer Completed." + colors.getAnsiReset());
					System.out.println();
				} catch (Exception e) {
					System.out.println(colors.getAnsiRed() + "NSF - Please Try Again" +  colors.getAnsiReset());
					System.out.println();
				}
				return cus;
			} else {
				System.out.println(colors.getAnsiRed() + "Invalid option, pleae try again" + colors.getAnsiReset());
			}			
		}
	}
	
	public void recent(Customer cus, Connection conn) {
		// Print out prompt for transfer
		System.out.println(colors.getAnsiBlue() + "+---------------------+" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "| RECENT Transactions |" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "+---------------------+" + colors.getAnsiReset());
		
		while (true) {
			// Ask for which account
			System.out.print("Which Account?\n1.)Checking\n2.)Savings\n\n" + colors.getAnsiGreen() + "Choice: " + colors.getAnsiReset());
			String response = scan.nextLine();
			int account = Integer.parseInt(response);
			System.out.println();
			
			// Decide weather its checking or savings
			if (account == 1) {
				// Convert transactions to an array and grab last 5
				Object[] transactions =  cus.getChecking().getTransactions().toArray();
				if (transactions.length <= 5) {
					for (int x = 0; x < transactions.length; x++) {
						System.out.println(transactions[x]);
						System.out.println();
					}
				} else {
					for (int x = transactions.length - 5; x < transactions.length; x++) {
						System.out.println(transactions[x]);
						System.out.println();
					}
				}
				break;
			} else if (account == 2) {
				// Convert transactions to an array and grab last 5
				Object[] transactions = cus.getSavings().getTransactions().toArray();
				if (transactions.length <= 5) {
					for (int x = 0; x < transactions.length; x++) {
						System.out.println(transactions[x]);
						System.out.println();
					}
				} else {
					for (int x = transactions.length - 5; x < transactions.length; x++) {
						System.out.println(transactions[x]);
						System.out.println();
					}
				}
				break;
			} else {
				System.out.println(colors.getAnsiRed() + "Pleae enter a valid response" + colors.getAnsiReset());
			}
		}
	}
	
	public void info(Customer cus) {
		// Print header
		System.out.println(colors.getAnsiBlue() + "+----------------------+" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "| CUSTOMER Information |" + colors.getAnsiReset());
		System.out.println(colors.getAnsiBlue() + "+----------------------+" + colors.getAnsiReset());
		
		// Grab and display information
		System.out.println("First Name: " + cus.getFirstName());
		System.out.println("Last Name: " + cus.getLastName());
		System.out.println("Username: " + cus.getUsername());
		System.out.println("Password: REDACTED");
		System.out.println("State: " + cus.getState());
		System.out.println("Phone Number: (" + cus.getArea_code() + ") " + cus.getLocal_code() + "-" + cus.getPersonal_digits());
		System.out.println();
	}
}
