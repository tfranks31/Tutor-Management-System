package tutorsdb;

import java.util.Scanner;

import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute accountByLogin query manually from the command line.
 */
public class AccountFromLoginInfoQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		System.out.print("Enter Username: ");
		String username = keyBoard.nextLine();
		System.out.print("Enter Password: ");
		String password = keyBoard.nextLine();
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		UserAccount account = db.accountByLogin(username, password);
		
		if (account == null) {
			System.out.println("No accounts found with Username: " + username + " Password: " + password);
		}else {
			System.out.println(account.getAccountID() + "," + account.getIsAdmin());		
		}
	}
}
