package tutorsdb;

import java.util.List;
import java.util.Scanner;

import model.Pair;
import model.PayVoucher;
import model.Tutor;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute findVoucherBySearch query manually from the command line.
 */
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
		List<Pair<Tutor, PayVoucher>> VoucherList = db.findVoucherBySearch(search);
		
		if (VoucherList.isEmpty()) {
			System.out.println("No Voucher found with Search Paramter: " + search);
		}else {
			for (Pair<Tutor, PayVoucher> tutorVoucher : VoucherList) {
				Tutor tutor = tutorVoucher.getLeft();
				PayVoucher voucher = tutorVoucher.getRight();
				System.out.println(tutor.getName() + "," + tutor.getSubject() + ',' + voucher.getIsSubmitted() + "," + voucher.getDueDate());
			}
		}
	}
}
