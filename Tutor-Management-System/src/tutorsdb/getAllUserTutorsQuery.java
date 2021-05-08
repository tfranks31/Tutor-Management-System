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
public class getAllUserTutorsQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		List<Pair<UserAccount, Tutor>> userTutorList = db.getAllUserTutor();
		
		if (userTutorList.isEmpty()) {
			System.out.println("No userTutors found");
		}else {
			for (Pair<UserAccount, Tutor> tutorList : userTutorList) {
				Tutor tutor = tutorList.getRight();
				UserAccount userAccount = tutorList.getLeft();
				System.out.println(tutor.getAccountID() + "," + tutor.getName() + ',' + userAccount.getUsername() + "," + 
				userAccount.getIsAdmin());
			}
		}
	}
}
