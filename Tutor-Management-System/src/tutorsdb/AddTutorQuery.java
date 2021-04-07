package tutorsdb;

import java.util.Scanner;

import model.Tutor;
import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute the addTutor query manually from the command line.
 */
public class AddTutorQuery {
	
	public static void main(String[] args) throws Exception {
		
		Scanner keyBoard = new Scanner(System.in);
		UserAccount account = new UserAccount();
		Tutor tutor = new Tutor();
		
		// Requests input for addTutor
		InitDatabase.init(keyBoard);
		System.out.print("Enter tutor's firstname: ");
		String firstname = keyBoard.nextLine();
		System.out.print("Enter tutor's lastname: ");
		String lastname = keyBoard.nextLine();
		tutor.setName(firstname + " " + lastname);
		System.out.print("Enter tutor's username: ");
		account.setUsername(keyBoard.nextLine());
		System.out.print("Enter tutor's password: ");
		account.setPassword(keyBoard.nextLine());
		System.out.print("Enter tutor's email: ");
		tutor.setEmail(keyBoard.nextLine());
		System.out.print("Enter tutor's student ID: ");
		tutor.setStudentID(keyBoard.nextLine());
		System.out.print("Enter tutor's account number: ");
		tutor.setAccountNumber(keyBoard.nextLine());
		System.out.print("Enter tutor's subject: ");
		tutor.setSubject(keyBoard.nextLine());
		System.out.print("Enter tutor's rate of pay: ");
		tutor.setPayRate(keyBoard.nextDouble());
		
		// Get the tutordb instance and execute the addTutor transaction
		IDatabase db = DatabaseProvider.getInstance();
		db.addTutor(account, tutor);
		
		System.out.println("Tutor Sucessfully added");
	}
}