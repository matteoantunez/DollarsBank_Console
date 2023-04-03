package com.dollarsbank.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RunningQueries {

	public static void main (String[] args) {
		
		// Connection conn = ConnectionManager.getConnection();
		
		try (Connection conn = ConnectionManager.getConnection()){
			
			// STATEMENT --> Represent an SQL statement we need to run
			
			// Create the object that represents statement
			Statement stmt = conn.createStatement();
			
			// Execute some sort of update (update, delete, insert, things that change data in a table)
			int count = stmt.executeUpdate("INSERT INTO address(street, city, state, zip_code) VALUES ('13 Jump Street', 'Chicago', 'IL', '12345')");
			
			System.out.println("Updates Made: " + count);
			
			count = stmt.executeUpdate("UPDATE ADDRESS SET street = '100 Jump Street' WHERE city = 'Chicago'");
			
			System.out.println("Updates Made: " + count);
			
			stmt.close();
			
			// PreparedStatement is going to have parameters / arguments for different parts of your query
			PreparedStatement pstmt = conn.prepareStatement("UPDATE ADDRESS SET street = ? WHERE city = ?");

			pstmt.setString(1, "1 Hello Street");
			pstmt.setString(2, "Chicago");
			
			count = pstmt.executeUpdate();
			
			System.out.println("Rows updated with Pr. Stmt: " + count);
			
			// RESULT SETS --> retrieve data from our dbs
			PreparedStatement allAddr = conn.prepareStatement("SELECT * FROM address");
			
			ResultSet rs = allAddr.executeQuery();
			
			System.out.println("Address Table");
			System.out.println("------------------------------");
			
			while(rs.next()) {
				int id = rs.getInt(1);
				String street = rs.getString("street");
				
				System.out.println("ID: " + id + " Street: " + street);
				
			}
			
			rs.close();
			allAddr.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
