package tutorsdb;

import java.util.List;
import java.util.Scanner;

import model.Entry;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

public class EntryByVoucherIdQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyBoard = new Scanner(System.in);
		
		// Create the default IDatabase instance
		InitDatabase.init(keyBoard);
		System.out.print("Enter Voucher ID: ");
		int voucherID = keyBoard.nextInt();

		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		List<Entry> entryList = db.findEntryByVoucher(voucherID);
		
		if (entryList.isEmpty()) {
			System.out.println("No entrys found with Voucher ID: " + voucherID);
		}else {
			for (Entry entry : entryList) {
				System.out.println(entry.getDate() + "," + entry.getHours() + "," + entry.getServicePerformed() + "," + entry.getWherePerformed());
			}
		}
	}
}
