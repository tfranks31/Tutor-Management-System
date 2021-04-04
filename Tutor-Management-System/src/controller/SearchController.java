package controller;

import java.util.ArrayList;
import java.util.List;

import model.PayVoucher;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;

public class SearchController {
	private IDatabase db = null;
	
	public SearchController() {
		
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	public ArrayList<PayVoucher> getAllVouchers() {
		
		ArrayList<PayVoucher> allVouchers = (ArrayList<PayVoucher>) db.findAllPayVouchers();
		
		if (allVouchers.isEmpty()) {
			return null;
		}
		
		return  allVouchers;
	}
	
	public ArrayList<PayVoucher> getVoucherFromSearch(String search){
		
		ArrayList<PayVoucher> result = (ArrayList<PayVoucher>) db.findVoucherBySearch(search);
		
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}
}
