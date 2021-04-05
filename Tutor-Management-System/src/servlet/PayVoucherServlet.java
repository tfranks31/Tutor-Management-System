package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.PayVoucherController;
import model.Entry;
import model.PayVoucher;
import model.Tuple;
import model.Tutor;

public class PayVoucherServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private int tableSize;
	private PayVoucherController controller = null;

	
	public PayVoucherServlet(){
		controller = new PayVoucherController();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("PayVoucher Servlet: doGet");	

		// Go back to search
		if (req.getParameter("back") != null) {
			
			resp.sendRedirect("search");
		}
		// Load addTutor
		else {
			
			ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = (ArrayList<Tuple<Tutor, PayVoucher, Entry>>) controller.getPayVoucherEntries(4);
			ArrayList<Entry> entries = new ArrayList<Entry>();
			PayVoucher voucher = tutorVoucherEntryList.get(0).getMiddle(); //all voucher instances are identical
			Tutor tutor = tutorVoucherEntryList.get(0).getLeft(); //add tutor instances are identical
			
			for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
				if (tutorVoucherEntry.getRight().getEntryID() != -1) {
					entries.add(tutorVoucherEntry.getRight());
				}
			}

			if (entries.size() < 10) {
				tableSize = 10 - entries.size();
			}
			else {
				tableSize = 0;
			}
			
			req.setAttribute("tableSize", tableSize);
			req.setAttribute("entries", entries);
			req.setAttribute("tutorName", tutor.getName());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("dueDate", voucher.getDueDate());
			req.setAttribute("accountNumber", tutor.getAccountID());
			req.setAttribute("totalHours",voucher.getTotalHours());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("totalPay", voucher.getTotalPay());
			
			// Call JSP to generate empty form
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("PayVoucher Servlet: doPost");	

		// Go back to search
		if (req.getParameter("addRow") != null) {	
			
			ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = (ArrayList<Tuple<Tutor, PayVoucher, Entry>>) controller.getPayVoucherEntries(4);
			
			ArrayList<Entry> entries = new ArrayList<Entry>();
			PayVoucher voucher = tutorVoucherEntryList.get(0).getMiddle(); //all voucher instances are identical
			Tutor tutor = tutorVoucherEntryList.get(0).getLeft(); //all tutor instances are identical
						
			String[] cells = req.getParameterValues("cell");
			
			for (int i = 0; i < cells.length; i += 4) {	
				if (!cells[i].equals("") && !cells[i + 1].equals("") &&
					!cells[i + 2].equals("") && !cells[i + 3].equals("")) {
					
					Entry entry = new Entry();
					entry.setDate(cells[i]);
					entry.setHours(Double.parseDouble(cells[i + 1]));
					entry.setServicePerformed(cells[i + 2]);
					entry.setWherePerformed(cells[i + 3]);
					entries.add(entry);
				}
			}
			
			
			tableSize = cells.length / 4 - entries.size() + 1;

			if(tableSize + entries.size() > 15) {
				tableSize = cells.length / 4 - entries.size();
			}
			
			req.setAttribute("tableSize", tableSize);	
			
			req.setAttribute("entries", entries);
			req.setAttribute("tutorName", tutor.getName());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("dueDate", voucher.getDueDate());
			req.setAttribute("accountNumber", tutor.getAccountID());
			req.setAttribute("totalHours",voucher.getTotalHours());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("totalPay", voucher.getTotalPay());
			
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		
		}
		
	}

}