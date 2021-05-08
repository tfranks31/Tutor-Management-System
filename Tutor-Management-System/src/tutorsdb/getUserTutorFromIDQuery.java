package tutorsdb;

import java.util.List;
import java.util.Scanner;

import model.Pair;
import model.Tutor;
import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute findAllPayVouchers query manually from the command line.
 */
public class getUserTutorFromIDQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		System.out.print("Enter Account ID: ");
		int ID = keyBoard.nextInt();
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		Pair<UserAccount, Tutor> userTutor = db.getUserTutorByAccountID(ID);
		
		if (userTutor.getLeft() == null && userTutor.getRight() == null) {
			System.out.println("No userTutors found");
		}else {
			Tutor tutor = userTutor.getRight();
			UserAccount userAccount = userTutor.getLeft();
			System.out.println(tutor.getAccountID() + "," + tutor.getName() + ',' + userAccount.getUsername() + "," + 
			userAccount.getIsAdmin());
		}
	}
}
