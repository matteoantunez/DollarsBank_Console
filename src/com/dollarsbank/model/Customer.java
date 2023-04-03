// Implemented in Database

package com.dollarsbank.model;

import java.io.Serializable;

public class Customer implements Serializable{
	
	private static final long serialVersionUID = 1L; 
	
	// Create Variables
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String state;
	private int area_code;
	private int local_code;
	private int personal_digits;
	private Account checking;
	private SavingsAccount savings;
	
	// Getters and Setters
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getArea_code() {
		return area_code;
	}
	public void setArea_code(int area_code) {
		this.area_code = area_code;
	}
	public int getLocal_code() {
		return local_code;
	}
	public void setLocal_code(int local_code) {
		this.local_code = local_code;
	}
	public int getPersonal_digits() {
		return personal_digits;
	}
	public void setPersonal_digits(int personal_digits) {
		this.personal_digits = personal_digits;
	}
	public Account getChecking() {
		return checking;
	}
	public void setChecking(Account checking) {
		this.checking = checking;
	}
	public SavingsAccount getSavings() {
		return savings;
	}
	public void setSavings(SavingsAccount savings) {
		this.savings = savings;
	}

	// Constructor
	public Customer(String firstName, String lastName, String username, String password, String state, int area_code, int local_code,
			int personal_digits) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.state = state;
		this.area_code = area_code;
		this.local_code = local_code;
		this.personal_digits = personal_digits;
	}
	
	
	
}
