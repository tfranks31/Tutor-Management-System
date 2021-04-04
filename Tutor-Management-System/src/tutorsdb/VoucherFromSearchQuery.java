package tutorsdb;

import java.util.List;
import java.util.Scanner;

import model.PayVoucher;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

public class VoucherFromSearchQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		/*
		 * Enter actual values not parameter
		 * Ex: you enter Zachary Roberts not name
		 */
		System.out.print("Enter Search Parameter: ");
		String search = keyBoard.nextLine();

		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		List<PayVoucher> VoucherList = db.findVoucherBySearch(search);
		
		if (VoucherList.isEmpty()) {
			System.out.println("No Voucher found with Search Paramter: " + search);
		}else {
			for (PayVoucher voucher : VoucherList) {
				System.out.println(voucher.getPayVoucherID() + "," + voucher.getTotalHours() + "," + voucher.getTotalPay() + "," + voucher.getStartDate() + "," + voucher.getDueDate());
			}
		}
	}
}
