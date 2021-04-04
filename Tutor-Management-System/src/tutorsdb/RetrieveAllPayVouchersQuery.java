package tutorsdb;

import java.util.List;
import java.util.Scanner;

import model.PayVoucher;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

public class RetrieveAllPayVouchersQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		List<PayVoucher> VoucherList = db.findAllPayVouchers();
		
		if (VoucherList.isEmpty()) {
			System.out.println("No pay voucher found");
		}else {
			for (PayVoucher voucher : VoucherList) {
				System.out.println(voucher.getPayVoucherID() + "," + voucher.getTotalHours() + "," + voucher.getTotalPay() + "," + voucher.getStartDate() + "," + voucher.getDueDate());
			}
		}
	}
}
