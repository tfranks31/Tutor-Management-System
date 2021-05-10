package tutorsdb;

import java.util.Scanner;

import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute markPayVoucherNotNew query manually from the command line.
 */
public class markPayVoucherNotNewQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
				
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		
		System.out.print("Enter Voucher ID: ");
		int voucherID = keyBoard.nextInt();
		
		db.markPayVoucherNotNew(voucherID);
				
		System.out.println("Voucher is no longer new!");
	}
}
