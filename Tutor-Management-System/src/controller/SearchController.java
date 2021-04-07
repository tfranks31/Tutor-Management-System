package controller;

import java.util.ArrayList;
import java.util.List;

import model.PayVoucher;
import model.Tutor;
import model.Pair;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;

public class SearchController {
	private IDatabase db = null;
	
	public SearchController() {
		
		db = DatabaseProvider.getInstance();
	}
	
	public ArrayList<Pair<Tutor, PayVoucher>> getAllVouchers() {
		
		List<Pair<Tutor, PayVoucher>> allVouchersList = db.findAllPayVouchers();
		
		ArrayList<Pair<Tutor, PayVoucher>> allVouchers = new ArrayList<Pair<Tutor, PayVoucher>>();
		for (Pair<Tutor, PayVoucher> voucherPair : allVouchersList) {
			
			allVouchers.add(voucherPair);
		}
		
		return allVouchers;
	}
	
	public ArrayList<Pair<Tutor, PayVoucher>> getVoucherFromSearch(String search){
		
		List<Pair<Tutor, PayVoucher>> resultList = db.findVoucherBySearch(search);
		
		ArrayList<Pair<Tutor, PayVoucher>> result = new ArrayList<Pair<Tutor, PayVoucher>>();
		for (Pair<Tutor, PayVoucher> voucherPair : resultList) {
			
			result.add(voucherPair);
		}
		
		return result;
	}
	
	public void assignPayVoucher(String startDate, String dueDate) {
		
		db.assignVoucher(startDate, dueDate);
	}
}
