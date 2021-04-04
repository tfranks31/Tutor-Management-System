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
		
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	public ArrayList<Pair<Tutor, PayVoucher>> getAllVouchers() {
		
		ArrayList<Pair<Tutor, PayVoucher>> allVouchers = (ArrayList<Pair<Tutor, PayVoucher>>) db.findAllPayVouchers();
		
		if (allVouchers.isEmpty()) {
			return null;
		}
		
		return  allVouchers;
	}
	
	public ArrayList<Pair<Tutor, PayVoucher>> getVoucherFromSearch(String search){
		
		ArrayList<Pair<Tutor, PayVoucher>> result = (ArrayList<Pair<Tutor, PayVoucher>>) db.findVoucherBySearch(search);
		
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}
}
