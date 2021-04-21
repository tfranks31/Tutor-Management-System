package tutorsdb;

import java.util.List;
import java.util.Scanner;

import model.Pair;
import model.PayVoucher;
import model.Tutor;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute findAllPayVouchers query manually from the command line.
 */
public class RetrieveAllPayVouchersQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		List<Pair<Tutor, PayVoucher>> VoucherList = db.findAllPayVouchers();
		
		if (VoucherList.isEmpty()) {
			System.out.println("No pay voucher found");
		}else {
			for (Pair<Tutor, PayVoucher> tutorVoucher : VoucherList) {
				Tutor tutor = tutorVoucher.getLeft();
				PayVoucher voucher = tutorVoucher.getRight();
				System.out.println(voucher.getPayVoucherID() + "," + tutor.getName() + "," + tutor.getSubject() + ',' + voucher.getIsSubmitted() + "," + voucher.getDueDate());
			}
		}
	}
}
