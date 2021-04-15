package tutorsdb;

import java.util.ArrayList;
import java.util.Scanner;

import model.Entry;
import model.PayVoucher;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * Execute updateVoucher query manually from the command line.
 */
public class UpdateVoucherWithEntriesQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
				
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		
		//sets up test list
		PayVoucher voucher = new PayVoucher();
		voucher.setPayVoucherID(-1);
		voucher.setTutorID(1);
		Entry entry1 = new Entry();
		entry1.setPayVoucherID(1);
		entry1.setDate("01/03/2021");
		entry1.setServicePerformed("tutoring");
		entry1.setHours(2);
		entry1.setWherePerformed("zoom");
		Entry entry2 = new Entry();
		entry2.setPayVoucherID(1);
		entry2.setDate("01/03/2021");
		entry2.setServicePerformed("tutoring");
		entry2.setHours(2);
		entry2.setWherePerformed("zoom");
		
		ArrayList<Entry> entries = new ArrayList<Entry>();
		
		db.updateVoucher(entries, voucher);
				
		System.out.println("Voucher Sucessfully Updated");
	}
}
