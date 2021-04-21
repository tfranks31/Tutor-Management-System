package tutorsdb;

import java.util.Scanner;

import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute assignVoucher query manually from the command line.
 */
public class AssignVoucherSpecific {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		System.out.print("Enter Start Date: ");
		String startDate = keyBoard.nextLine();
		System.out.print("Enter Due Date: ");
		String dueDate = keyBoard.nextLine();
		System.out.print("Enter Tutors Name: ");
		String name = keyBoard.nextLine();
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		db.assignVoucherSpecific(startDate, dueDate, name);
		
		System.out.println("Vouchers Sucessfully Assigned");
	}
}
