package tutorsdb;

import java.util.Scanner;

import model.Pair;
import model.Tutor;
import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute getTutorInfo query manually from the command line.
 */
public class editTutorQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		/*
		 * Enter actual values not parameter
		 * Ex: you enter Zachary Roberts not name
		 */
		System.out.print("Enter Tutor's name: ");
		String name = keyBoard.nextLine();

		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		Pair<UserAccount, Tutor> AccountTutor = db.getTutorInfo(name);
	
		UserAccount UserAccount = AccountTutor.getLeft();
		Tutor tutor = AccountTutor.getRight();
		
		System.out.println("Leave blank to keep the same");
		
		System.out.print("Enter tutor's firstname: ");
		String firstname = keyBoard.nextLine();
		System.out.print("Enter tutor's lastname: ");
		String lastname = keyBoard.nextLine();
		
		if (firstname != "" && lastname != "") {
			tutor.setName(firstname + " " + lastname);
		}
		
		System.out.print("Enter tutor's username: ");
		String username = keyBoard.nextLine();
		
		if (username != "") {
			UserAccount.setUsername(username);
		}
		
		System.out.print("Enter tutor's password: ");
		String password = keyBoard.nextLine();
		
		if (password != "") {
			UserAccount.setPassword(password);
		}
		
		System.out.print("Enter tutor's email: ");
		String email = keyBoard.nextLine();
		
		if (email != "") {
			tutor.setEmail(email);
		}
		
		System.out.print("Enter tutor's student ID: ");
		String StudentID = keyBoard.nextLine();
		
		if (StudentID != "") {
			tutor.setStudentID(StudentID);
		}
		
		System.out.print("Enter tutor's account number: ");
		String accountnumber = keyBoard.nextLine();
		
		if (accountnumber != "") {
			tutor.setAccountNumber(accountnumber);
		}
		
		System.out.print("Enter tutor's subject: ");
		String subject = keyBoard.nextLine();
		
		if (subject != "") {	
			tutor.setSubject(subject);
		}
		
		System.out.print("Enter tutor's rate of pay (0 to stay the same): ");
		double pay = keyBoard.nextDouble();
		
		if (pay != 0.0) {	
			tutor.setPayRate(pay);
		}
		
		db.editTutor(UserAccount, tutor);
		
		System.out.print("Tutor Succesfully Updated");
		
	}
}
