package tutorsdb;

import java.util.ArrayList;
import java.util.Scanner;

import model.Entry;
import model.PayVoucher;
import model.Tuple;
import model.Tutor;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute findEntryByVoucher query manually from the command line.
 */
public class EntryByVoucherIdQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		System.out.print("Enter Voucher ID: ");
		int voucherID = keyBoard.nextInt();

		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		
		//generates required lists and object
		ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = (ArrayList<Tuple<Tutor, PayVoucher, Entry>>) db.findEntryByVoucher(voucherID);
		
		if (tutorVoucherEntryList.isEmpty()) {
			System.out.println("No entrys found with Voucher ID: " + voucherID);
		}else {
			for (Tuple<Tutor, PayVoucher, Entry> tutorvoucherEntry : tutorVoucherEntryList) {
				Tutor tutor = tutorvoucherEntry.getLeft();
				PayVoucher voucher = tutorvoucherEntry.getMiddle();
				Entry entry = tutorvoucherEntry.getRight();
				
				System.out.println(tutor.getName() + "," + tutor.getAccountID() + "," + tutor.getStudentID() + "," + voucher.getDueDate() + ","
									+ entry.getDate() + "," + entry.getHours() + "," + entry.getServicePerformed() + "," + entry.getWherePerformed());
			}
		}
	}
}
