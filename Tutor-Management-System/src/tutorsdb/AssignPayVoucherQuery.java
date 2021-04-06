package tutorsdb;

import java.util.Scanner;

import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

public class AssignPayVoucherQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		System.out.print("Enter Start Date: ");
		String startDate = keyBoard.nextLine();
		System.out.print("Enter Due Date: ");
		String dueDate = keyBoard.nextLine();
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		db.assignVoucher(startDate, dueDate);
		
		System.out.println("Vouchers Sucessfully Assigned");
	}
}
