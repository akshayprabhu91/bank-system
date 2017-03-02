package java;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

/**
 *
 * @author PrabhuA6510
 */
@Named(value = "createNewAccount")
@ManagedBean
@RequestScoped
public class CreateNewAccount {

	private String ssn;
	private double balance;

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	// create new bank Account
	// return successful/non-successful message
	public String createAccount() {
		// load the driver
		try {
			Class.forName("com.mysql.jdbc.driver");
		} catch (Exception e) {
			return ("Internal Error for Driver. Please try again later!");
		}

		final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/DrLin";
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;

		String accountID = "";

		try {
			// connect to the DB
			conn = DriverManager.getConnection(DB_URL, "DrLin", "UHCL2014");
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from nextAccountNumber");

			if (rs.next()) {
				// get the next account ID
				accountID = "" + rs.getInt(1);
			}

			// activity String
			DecimalFormat df = new DecimalFormat("##.00");
			String s = DateAndTime.DateTime() + ": Account opened with an initial balance $" + df.format(balance)
					+ "\n";
			// insert a record into the DB
			int r = stat.executeUpdate("insert into BankAccount values ('" + accountID + "', '" + ssn + "', '" + balance
					+ "', '" + s + "')");
			return ("Congratulations! You have created a new bank Account! The account number is " + accountID);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stat.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
