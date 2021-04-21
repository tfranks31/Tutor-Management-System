package tutorsdb;

import java.util.Scanner;

import model.PayVoucher;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute submitPayVoucher query manually from the command line.
 */
public class SignPayVoucherQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		
		System.out.print("Enter Voucher ID: ");
		int voucherID = keyBoard.nextInt()
				;
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		PayVoucher voucher = db.signPayVoucher(voucherID);
				
		if (voucher == null) {
			System.out.println("Pay voucher with ID: " + voucherID + " does not exist");
		}else {
			System.out.println("Pay voucher with ID: " + voucher.getPayVoucherID() + " was sucuessfully submitted");
		}
	}
}
