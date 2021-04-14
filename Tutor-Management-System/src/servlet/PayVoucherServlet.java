package servlet;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.PayVoucherController;
import model.Entry;
import model.PayVoucher;
import model.Tuple;
import model.Tutor;
import model.UserAccount;

public class PayVoucherServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private int tableSize, payVoucherID = 1;
	private PayVoucherController controller = null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("PayVoucher Servlet: doGet");	
		
		controller = new PayVoucherController();
		
		UserAccount account = (UserAccount) req.getSession().getAttribute("user");
		// If user not logged in, redirect to login
		if (account == null) {
			
			resp.sendRedirect("login");
		}
		
		// If user manually tries to access payvoucher view redirect to search
		else if (req.getParameter("ID") == null) {
			
			resp.sendRedirect("search");
		}

		// Go back to search
		else if (req.getParameter("back") != null) {
			
			resp.sendRedirect("search");
		}
		// Load and generate rows
		else {
			
			// Load payVoucher with the specified id
			if (req.getParameter("ID") != null) {
				
				payVoucherID = Integer.parseInt(req.getParameter("ID"));
			}
			ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = controller.getPayVoucherEntries(payVoucherID);
			ArrayList<Entry> entries = new ArrayList<Entry>();
			PayVoucher voucher = tutorVoucherEntryList.get(0).getMiddle(); //all voucher instances are identical
			Tutor tutor = tutorVoucherEntryList.get(0).getLeft(); //add tutor instances are identical
			
			// Load entries
			for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
				if (tutorVoucherEntry.getRight().getEntryID() != -1) {
					entries.add(tutorVoucherEntry.getRight());
				}
			}

			// Extra table spaces will be the hard limit of 10 - how many pre-existing entries
			if (entries.size() < 10) {
				tableSize = 10 - entries.size();
			}
			// More entries than 10, so generate no empty extra spaces
			else {
				tableSize = 0;
			}
			
			// Set attributes
			req.setAttribute("tableSize", tableSize);
			req.setAttribute("entries", entries);
			req.setAttribute("tutorName", tutor.getName());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("dueDate", voucher.getDueDate());
			req.setAttribute("accountNumber", tutor.getAccountNumber());
			req.setAttribute("totalHours",voucher.getTotalHours());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("totalPay", voucher.getTotalPay());
			
			// Call JSP to generate updated form
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("PayVoucher Servlet: doPost");
		
		controller = new PayVoucherController();
		
		// User just selected a payVoucher
		if (req.getParameter("ID") != null) {
			
			// Get the payVoucher ID
			payVoucherID = Integer.parseInt(req.getParameter("ID"));
			
			// Get tutor, voucher, and entries
			ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = controller.getPayVoucherEntries(payVoucherID);
			ArrayList<Entry> entries = new ArrayList<Entry>();
			PayVoucher voucher = tutorVoucherEntryList.get(0).getMiddle(); //all voucher instances are identical
			Tutor tutor = tutorVoucherEntryList.get(0).getLeft(); //add tutor instances are identical
			
			// Populate entries
			for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
				entries.add(tutorVoucherEntry.getRight());
			}

			// Extra table spaces will be the hard limit of 10 - how many pre-existing entries
			if (entries.size() < 10) {
				tableSize = 10 - entries.size();
			}
			// More entries than 10, so generate no empty extra spaces
			else {
				tableSize = 0;
			}
			
			// Set attributes
			req.setAttribute("tableSize", tableSize);
			req.setAttribute("entries", entries);
			req.setAttribute("tutorName", tutor.getName());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("dueDate", voucher.getDueDate());
			req.setAttribute("accountNumber", tutor.getAccountNumber());
			req.setAttribute("totalHours",voucher.getTotalHours());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("totalPay", voucher.getTotalPay());
			
			// Call JSP to generate updated form
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}

		// User wants to add a row
		else if (req.getParameter("addRow") != null) {	
			
			ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = controller.getPayVoucherEntries(payVoucherID);
			
			// Get tutor, voucher, and entries
			ArrayList<Entry> entries = new ArrayList<Entry>();
			PayVoucher voucher = tutorVoucherEntryList.get(0).getMiddle(); //all voucher instances are identical
			Tutor tutor = tutorVoucherEntryList.get(0).getLeft(); //all tutor instances are identical
						
			// Get all input cells from the pay voucher table
			String[] cells = req.getParameterValues("cell");
			
			// Populate entries
			for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
				entries.add(tutorVoucherEntry.getRight());
			}
			
			// Runs through all the cells refreshing old entries and creating new entries need be
			for (int i = 0; i < cells.length; i += 4) {	
				
				// Cell is not empty
				if (!cells[i].equals("") && !cells[i + 1].equals("") &&
					!cells[i + 2].equals("") && !cells[i + 3].equals("")) {
					
					Entry entry;
					
					// cells[i:i+3] contain a previous entry
					if (i < entries.size() * 4) {
						
						entry = entries.get(i / 4);
						
						// Empty vouchers will have an initial null entry, create a new entry
						if (entry == null) {
							
							entry = new Entry();
						}
						entry.setDate(cells[i]);
						entry.setHours(Double.parseDouble(cells[i + 1]));
						entry.setServicePerformed(cells[i + 2]);
						entry.setWherePerformed(cells[i + 3]);
					}
					
					// Cells[i:i+3] are new entries
					else {
						entry = new Entry();
						entry.setDate(cells[i]);
						entry.setHours(Double.parseDouble(cells[i + 1]));
						entry.setServicePerformed(cells[i + 2]);
						entry.setWherePerformed(cells[i + 3]);
						entries.add(entry);
					}
				}
			}
			
			// Set the remaining empty rows to be one more than the previous total row length
			tableSize = cells.length / 4 - entries.size() + 1;

			// Limit on how many rows can be generated
			if(tableSize + entries.size() > 15) {
				tableSize = cells.length / 4 - entries.size();
			}
			
			// Set attributes
			req.setAttribute("tableSize", tableSize);	
			
			req.setAttribute("entries", entries);
			req.setAttribute("tutorName", tutor.getName());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("dueDate", voucher.getDueDate());
			req.setAttribute("accountNumber", tutor.getAccountNumber());
			req.setAttribute("totalHours",voucher.getTotalHours());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("totalPay", voucher.getTotalPay());
			
			// Refresh payVoucher
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		
		}
		
		// User wants to update the voucher
		else if (req.getParameter("updateVoucher") != null) {
			
			ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = controller.getPayVoucherEntries(payVoucherID);
			
			// Get tutor, voucher, and entries
			ArrayList<Entry> entries = new ArrayList<Entry>();
			PayVoucher voucher = tutorVoucherEntryList.get(0).getMiddle(); //all voucher instances are identical
			Tutor tutor = tutorVoucherEntryList.get(0).getLeft(); //all tutor instances are identical
			
			// Get all input cells from the pay voucher table
			String[] cells = req.getParameterValues("cell");
			
			// Populate entries
			for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
				
				// Do not add any null entries, this is important for updating the database
				if (tutorVoucherEntry.getRight() != null) {
					
					entries.add(tutorVoucherEntry.getRight());
				}
			}
			
			// Runs through all the cells refreshing old entries and creating new entries need be
			for (int i = 0; i < cells.length; i += 4) {
				
				Entry entry;
				
				// cells[i:i+3] contain a previous entry
				if (i < entries.size() * 4) {
					
					entry = entries.get(i / 4);
					entry.setDate(cells[i]);
					
					// Set hours to 0 if blank, cannot set hours to a string or null
					if (cells[i + 1].equals("")) {
						entry.setHours(0);
					}
					else {
						entry.setHours(Double.parseDouble(cells[i + 1]));
					}
					
					entry.setServicePerformed(cells[i + 2]);
					entry.setWherePerformed(cells[i + 3]);
				}
				
				// Cells[i:i+3] are new entries
				else {
					
					// Skip blank entries
					if (!cells[i].equals("") && !cells[i + 1].equals("") &&
						!cells[i + 2].equals("") && !cells[i + 3].equals("")) {
							
						entry = new Entry();
						entry.setDate(cells[i]);
						entry.setHours(Double.parseDouble(cells[i + 1]));
						entry.setServicePerformed(cells[i + 2]);
						entry.setWherePerformed(cells[i + 3]);
						entries.add(entry);
						
					}
				}
				
				
			}
			
			// Update hours and total pay
			voucher.setTotalHours(controller.calculateTotalHours(entries));
			voucher.setTotalPay(controller.calculateTotalPay(tutor, voucher));
			
			controller.updateVoucherWithEntries(entries, voucher);
			
			// Get the refreshed entry list to account for removed entries
			tutorVoucherEntryList = controller.getPayVoucherEntries(payVoucherID);
			entries = new ArrayList<Entry>();
			
			// Populate entries
			for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
				
				if (tutorVoucherEntry.getRight() != null) {
					
					entries.add(tutorVoucherEntry.getRight());
				}
			}
			
			// Calculate remaining empty rows and set the empty row size
			tableSize = cells.length / 4 - entries.size();
			
			// Set attributes
			req.setAttribute("tableSize", tableSize);	
			
			req.setAttribute("entries", entries);
			req.setAttribute("tutorName", tutor.getName());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("dueDate", voucher.getDueDate());
			req.setAttribute("accountNumber", tutor.getAccountNumber());
			req.setAttribute("totalHours",voucher.getTotalHours());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("totalPay", voucher.getTotalPay());
			
			// Refresh payVoucher
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
			
		}
		
		// User wants to submit the voucher
		else if (req.getParameter("submitVoucher") != null) {
			
			ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = controller.getPayVoucherEntries(payVoucherID);
			
			// Get tutor, voucher, and entries
			ArrayList<Entry> entries = new ArrayList<Entry>();
			PayVoucher voucher = tutorVoucherEntryList.get(0).getMiddle(); //all voucher instances are identical
			Tutor tutor = tutorVoucherEntryList.get(0).getLeft(); //all tutor instances are identical
						
			// Get all input cells from the pay voucher table
			String[] cells = req.getParameterValues("cell");
			
			// Populate entries
			for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
				
				// Do not add any null entries, this is important for updating the database
				if (tutorVoucherEntry.getRight() != null) {
					
					entries.add(tutorVoucherEntry.getRight());
				}
			}
			
			// Runs through all the cells refreshing old entries and creating new entries need be
			for (int i = entries.size() * 4; i < cells.length; i += 4) {
				
				// Cell is not empty
				if (!cells[i].equals("") && !cells[i + 1].equals("") &&
					!cells[i + 2].equals("") && !cells[i + 3].equals("")) {
					
					Entry entry;
					
					// cells[i:i+3] contain a previous entry
					if (i < entries.size() * 4) {
						
						entry = entries.get(i / 4);
						entry.setDate(cells[i]);
						entry.setHours(Double.parseDouble(cells[i + 1]));
						entry.setServicePerformed(cells[i + 2]);
						entry.setWherePerformed(cells[i + 3]);
					}
					// Cells[i:i+3] are new entries
					else {
						entry = new Entry();
						entry.setDate(cells[i]);
						entry.setHours(Double.parseDouble(cells[i + 1]));
						entry.setServicePerformed(cells[i + 2]);
						entry.setWherePerformed(cells[i + 3]);
						entries.add(entry);
					}
				}
			}
			
			// Update hours and total pay
			voucher.setTotalHours(controller.calculateTotalHours(entries));
			voucher.setTotalPay(controller.calculateTotalPay(tutor, voucher));
			
			controller.updateVoucherWithEntries(entries, voucher);
			
			// Submit and get the updated voucher
			voucher = controller.submitPayVoucher(voucher.getPayVoucherID());
			
			// Calculate remaining empty rows and set the empty row size
			tableSize = cells.length / 4 - entries.size();
			
			// Set attributes
			req.setAttribute("tableSize", tableSize);	
			
			req.setAttribute("entries", entries);
			req.setAttribute("tutorName", tutor.getName());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("dueDate", voucher.getDueDate());
			req.setAttribute("accountNumber", tutor.getAccountNumber());
			req.setAttribute("totalHours",voucher.getTotalHours());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("totalPay", voucher.getTotalPay());
			
			// Refresh payVoucher
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
			
		}
		
		// User wants to sign off the voucher
		else if (req.getParameter("signVoucher") != null) {
			
			ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = controller.getPayVoucherEntries(payVoucherID);
			
			// Get tutor, voucher, and entries
			ArrayList<Entry> entries = new ArrayList<Entry>();
			PayVoucher voucher = tutorVoucherEntryList.get(0).getMiddle(); //all voucher instances are identical
			Tutor tutor = tutorVoucherEntryList.get(0).getLeft(); //all tutor instances are identical
						
			// Get all input cells from the pay voucher table
			String[] cells = req.getParameterValues("cell");
			
			// Populate entries
			for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
				
				// Do not add any null entries, this is important for updating the database
				if (tutorVoucherEntry.getRight() != null) {
					
					entries.add(tutorVoucherEntry.getRight());
				}
			}
			
			// Runs through all the cells refreshing old entries and creating new entries need be
			for (int i = entries.size() * 4; i < cells.length; i += 4) {
				
				// Cell is not empty
				if (!cells[i].equals("") && !cells[i + 1].equals("") &&
					!cells[i + 2].equals("") && !cells[i + 3].equals("")) {
					
					Entry entry;
					
					// cells[i:i+3] contain a previous entry
					if (i < entries.size() * 4) {
						
						entry = entries.get(i / 4);
						entry.setDate(cells[i]);
						entry.setHours(Double.parseDouble(cells[i + 1]));
						entry.setServicePerformed(cells[i + 2]);
						entry.setWherePerformed(cells[i + 3]);
					}
					
					// Cells[i:i+3] are new entries
					else {
						entry = new Entry();
						entry.setDate(cells[i]);
						entry.setHours(Double.parseDouble(cells[i + 1]));
						entry.setServicePerformed(cells[i + 2]);
						entry.setWherePerformed(cells[i + 3]);
						entries.add(entry);
					}
				}
			}
			
			// Update hours and total pay
			voucher.setTotalHours(controller.calculateTotalHours(entries));
			voucher.setTotalPay(controller.calculateTotalPay(tutor, voucher));
			
			controller.updateVoucherWithEntries(entries, voucher);
			
			// Sign and get the updated voucher
			voucher = controller.signPayVoucher(voucher.getPayVoucherID());
			
			// Calculate remaining empty rows and set the empty row size
			tableSize = cells.length / 4 - entries.size();
			
			// Set attributes
			req.setAttribute("tableSize", tableSize);	
			
			req.setAttribute("entries", entries);
			req.setAttribute("tutorName", tutor.getName());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("dueDate", voucher.getDueDate());
			req.setAttribute("accountNumber", tutor.getAccountNumber());
			req.setAttribute("totalHours",voucher.getTotalHours());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("totalPay", voucher.getTotalPay());
			
			// Refresh payVoucher
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}

}