package tutorsdb;

import java.util.Scanner;

import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

public class AddTutorQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		
		// requests input for add tutor
		InitDatabase.init(keyBoard);
		System.out.print("Enter tutor's firstname: ");
		String firstname = keyBoard.nextLine();
		System.out.print("Enter tutor's lastname: ");
		String lastname = keyBoard.nextLine();
		System.out.print("Enter tutor's username: ");
		String username = keyBoard.nextLine();;
		System.out.print("Enter tutor's password: ");
		String password = keyBoard.nextLine();
		System.out.print("Enter tutor's email: ");
		String email = keyBoard.nextLine();
		System.out.print("Enter tutor's student ID: ");
		String studentID = keyBoard.nextLine();
		System.out.print("Enter tutor's account number: ");
		String accountNumber = keyBoard.nextLine();
		System.out.print("Enter tutor's subject: ");
		String subject = keyBoard.nextLine();
		System.out.print("Enter tutor's rate of pay: ");
		double payRate = keyBoard.nextDouble();
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		db.AddTutor(firstname, lastname, username, password, email, studentID, accountNumber, subject, payRate);
		
		System.out.println("Tutor Sucessfully added");
	}
}
