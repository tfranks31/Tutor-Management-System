package tutorsdb;

import java.util.Scanner;

import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute updateVoucher query manually from the command line.
 */
public class UpdatePasswordQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
				
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		
		System.out.print("Enter new Password: ");
		String password = keyBoard.nextLine();
		System.out.print("Enter Account ID: ");
		int ID = keyBoard.nextInt();
	
		//sets up test list
		UserAccount account = new UserAccount();
		account.setAccountID(ID);
		
		db.updatePasswordWithUserID(account, password);
				
		System.out.println("Password Sucessfully Updated");
	}
}
