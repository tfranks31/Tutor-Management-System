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
public class getTutorInfoQuery {
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
		System.out.println( "Name,Username,Subject");
		System.out.println(tutor.getName() + ","+ UserAccount.getUsername() + "," + tutor.getSubject());
		
	}
}
