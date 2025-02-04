package servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
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
	private DecimalFormat decimalFormat = new DecimalFormat("##0.00");
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("PayVoucher Servlet: doGet");
		
		controller = new PayVoucherController();
		
		// Get the account logged in
		UserAccount account = (UserAccount) req.getSession().getAttribute("user");
		
		// If user not logged in, redirect to login
		if (account == null) {
			
			System.out.println("PayVoucher Servlet: null user");
			
			resp.sendRedirect("login");
		}
		
		// If user manually tries to access payvoucher view redirect to search
		else if (req.getParameter("ID") == null) {
			
			System.out.println("PayVoucher Servlet: No selected Pay Voucher");
			
			resp.sendRedirect("search");
		}

		// Go back to search
		else if (req.getParameter("back") != null) {
			
			System.out.println("PayVoucher Servlet: back");
			
			resp.sendRedirect("search");
		}
		
		// Load and generate rows
		else {
			
			System.out.println("PayVoucher Servlet: load");
			
			// Load payVoucher with the specified id
			if (req.getParameter("ID") != null) {
				
				payVoucherID = Integer.parseInt(req.getParameter("ID"));
			}
			
			// Get the voucher's entries
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
			
			//updates total pay in case tutor account was edited since voucher was last loaded
			voucher.setTotalPay(controller.calculateTotalPay(tutor, voucher));
			
			String[] cells = new String[entries.size() * 4];
			
			// Set cells to be displayed on the pay voucher
			for (int i = 0; i < cells.length; i += 4) {
				
				Entry entry = entries.get(i / 4);
				cells[i] = entry.getDate();
				cells[i + 1] = Double.toString(entry.getHours());
				cells[i + 2] = entry.getServicePerformed();
				cells[i + 3] = entry.getWherePerformed();
			}
			
			// Set attributes
			req.setAttribute("tableSize", tableSize);
			req.setAttribute("cells", cells);
			req.setAttribute("cellLength", cells.length - 1);
			req.setAttribute("tutorName", tutor.getName());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("dueDate", voucher.getDueDate());
			req.setAttribute("accountNumber", tutor.getAccountNumber());
			req.setAttribute("totalHours",voucher.getTotalHours());
			
			req.setAttribute("payRate", decimalFormat.format(tutor.getPayRate()));
			req.setAttribute("totalPay", decimalFormat.format(voucher.getTotalPay()));
			
			// Call JSP to generate updated form
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("PayVoucher Servlet: doPost");
		
		controller = new PayVoucherController();

		if (req.getParameter("ID") != null) {
			
			// Get the payVoucher ID
			payVoucherID = Integer.parseInt(req.getParameter("ID"));
		}
		
		// Get tutor, voucher, and entries
		ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntryList = controller.getPayVoucherEntries(payVoucherID);
		ArrayList<Entry> entries = new ArrayList<Entry>();
		PayVoucher voucher = tutorVoucherEntryList.get(0).getMiddle(); //all voucher instances are identical
		Tutor tutor = tutorVoucherEntryList.get(0).getLeft(); //add tutor instances are identical
		boolean callRefresh = true;
		
		// Populate entries
		for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
			
			// Do not add any null entries, this is important for updating the database
			if (tutorVoucherEntry.getRight() != null) {
				
				entries.add(tutorVoucherEntry.getRight());
			}
		}
		
		// Get all input cells from the pay voucher table
		String[] cells;
		
		// User just selected a payVoucher
		if (req.getParameter("ID") != null) {
			
			cells = new String[entries.size() * 4];
			
			// Update cells with entry information
			for (int i = 0; i < cells.length; i += 4) {
				
				Entry entry = entries.get(i / 4);
				cells[i] = entry.getDate();
				cells[i + 1] = Double.toString(entry.getHours());
				cells[i + 2] = entry.getServicePerformed();
				cells[i + 3] = entry.getWherePerformed();
			}

			// Extra table spaces will be the hard limit of 10 - how many pre-existing entries
			if (entries.size() < 10) {
				tableSize = 10 - entries.size();
			}
			// More entries than 10, so generate no empty extra spaces
			else {
				tableSize = 0;
			}
		}
		else {
					
			tableSize = 0;
			cells = req.getParameterValues("cell");
			
			// Validate that all cells do not have errors
			if (validate(req, cells)) {
				
				// Runs through all the cells refreshing old entries and creating new entries need be
				for (int i = 0; i < cells.length; i += 4) {
					
					Entry entry;
					
					// cells[i:i+3] contain a previous entry
					if (i < entries.size() * 4) {
						
						// Empty entry that was a previous entry
						if (cells[i].equals("") && cells[i + 1].equals("") &&
							cells[i + 2].equals("") && cells[i + 3].equals("")) {
							
							// Remove the entry from the list if just adding row
							if (req.getParameter("addRow") != null) {
								
								entries.remove(i / 4);
								i -= 4;
							}
							
							// Set entry to be removed from the database if not adding row
							else {
								
								entry = entries.get(i / 4);
								entry.setDate(cells[i]);
								entry.setHours(0);
								entry.setServicePerformed(cells[i + 2]);
								entry.setWherePerformed(cells[i + 3]);
							}
						}
						
						// Update the entry if all cells are filled, if not, do not change the entry at all
						else if (!cells[i].equals("") && !cells[i + 1].equals("") &&
								 !cells[i + 2].equals("") && !cells[i + 3].equals("")) {

							entry = entries.get(i / 4);
							entry.setDate(cells[i]);
							entry.setHours(Double.parseDouble(cells[i + 1]));
							entry.setServicePerformed(cells[i + 2]);
							entry.setWherePerformed(cells[i + 3]);
						}					
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
				
				// User wants to add a row
				if (req.getParameter("addRow") != null) {	
					
					// Set the remaining empty rows to be one more than the previous total row length
					tableSize = 1;

					// Limit on how many rows can be generated
					if(cells.length >= 60) {
						tableSize = 0;
					}
				}
				else {

					// Update hours and total pay
					voucher.setTotalHours(controller.calculateTotalHours(entries));
					voucher.setTotalPay(controller.calculateTotalPay(tutor, voucher));
					
					UserAccount account = (UserAccount) req.getSession().getAttribute("user");
					
					// Remove "new" status if the voucher is being updated by it's tutor
					if (voucher.getIsNew() && !account.getIsAdmin()) {
						
						controller.markPayVoucherNotNew(voucher.getPayVoucherID());
					}
					
					// Remove the edited by admin status if the voucher is being updated by it's tutor
					if (voucher.getIsAdminEdited() && !account.getIsAdmin()) {
						
						controller.markPayVoucherEditedByAdmin(voucher.getPayVoucherID(), false);
					}
					
					// Mark the voucher as being edited by an admin if an admin is updating the voucher
					if (account.getIsAdmin()) {
						
						controller.markPayVoucherEditedByAdmin(voucher.getPayVoucherID(), true);
					}
					
					controller.updateVoucherWithEntries(entries, voucher);
					
					// Get the refreshed entry list to account for removed entries
					tutorVoucherEntryList = controller.getPayVoucherEntries(payVoucherID);
					entries = new ArrayList<Entry>();
					
					// Refresh updated entries
					for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntryList) {
						
						if (tutorVoucherEntry.getRight() != null) {
							
							entries.add(tutorVoucherEntry.getRight());
						}
					}
					
					// Calculate remaining empty rows and set the empty row size
					tableSize = 0;
					
					// User wants to submit the voucher
					if (req.getParameter("submitVoucher") != null) {
						
						// Submit and get the updated voucher
						voucher = controller.submitPayVoucher(voucher.getPayVoucherID());
					}
					
					// User wants to sign off the voucher
					else if (req.getParameter("signVoucher") != null) {
						
						// Sign and get the updated voucher
						voucher = controller.signPayVoucher(voucher.getPayVoucherID());
					}
					
					// User wants to export their pay voucher
					else if (req.getParameter("exportVoucher") != null) {
						
						// Set flag to not refresh the page
						callRefresh = false;
						exportPayVoucherToCSV(cells, voucher, tutor, req, resp);
					}
				}
			}		
		}
				
		// Set attributes
		req.setAttribute("tableSize", tableSize);
		
		//updates total pay in case tutor account was edited since voucher was last loaded
		voucher.setTotalPay(controller.calculateTotalPay(tutor, voucher));
		
		req.setAttribute("cells", cells);
		req.setAttribute("cellLength", cells.length - 1);
		req.setAttribute("tutorName", tutor.getName());
		req.setAttribute("studentID", tutor.getStudentID());
		req.setAttribute("dueDate", voucher.getDueDate());
		req.setAttribute("accountNumber", tutor.getAccountNumber());
		req.setAttribute("totalHours",voucher.getTotalHours());
		req.setAttribute("payRate", decimalFormat.format(tutor.getPayRate()));
		req.setAttribute("totalPay", decimalFormat.format(voucher.getTotalPay()));
		
		// Refresh payVoucher		
		if (callRefresh) {
			
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
	}
	
	/**
	 * Validate the inputed cells.
	 * @param req The request to set an error message to.
	 * @param cells The cells to validate.
	 * @return True if the cells are valid, false if they are not valid.
	 */
	private boolean validate(HttpServletRequest req, String[] cells) {
		
		// Check each row of cells
		for (int i = 0; i < cells.length; i += 4) {
			
			// Skip over empty rows
			if (!cells[i].equals("") || !cells[i + 1].equals("") ||
				!cells[i + 2].equals("") || !cells[i + 3].equals("")) {
				
				// Check for a valid date format
				if (!cells[i].matches("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$")) {
					
					req.setAttribute("errorMessage", "Date format must be MM/DD/YYYY");
					return false;
				}
				
				//checks to for hours to be decimal
				try {
					
					Double.valueOf(cells[i + 1]);
				}
				catch (NumberFormatException e) {
					req.setAttribute("errorMessage", "Hours can only contain decimal numbers");
					return false;
				}
				
				//checks for hours to be postitive
				if (Double.valueOf(cells[i + 1]) <= 0) {
					req.setAttribute("errorMessage", "Hours must be positive");
					return false;
				}
				//checks if hours is over 24
				if (Double.valueOf(cells[i + 1]) > 24) {
					req.setAttribute("errorMessage", "Hours can't be over 24");
					return false;
				}
				
				// Checks if a service performed was entered
				if (cells[i + 2].equals("")) {
					
					req.setAttribute("errorMessage", "Please input what service was performed");
					return false;
				}
				
				//checks size for servicePerformed, Max = 250 char
				if (!(cells[i + 2].length() <= 250)) {
					req.setAttribute("errorMessage", "Service performed must be less than 250 characters");
					return false;
				}
					
				// Checks if where performed was entered
				if (cells[i + 3].equals("")) {
					
					req.setAttribute("errorMessage", "Please input where the tutoring was performed");
					return false;
				}
				
				//Checks size for wherePerformed, Max = 120 char
				if (!(cells[i + 3].length() <= 120)) {
					req.setAttribute("errorMessage", "Where performed must be less than 120 characters");
					return false;
				}			
			}		
		}
		
		return true;
	}
	
	/**
	 * Export the inputed cells to a CSV file.
	 * @param cells The cells to export to CSV.
	 * @param voucher The current pay voucher, used in naming the file.
	 * @param tutor The tutor that owns the pay voucher, used in naming the file.
	 * @param req The request to set an error message to.
	 * @param resp The response to forward the CSV data to.
	 */
	private void exportPayVoucherToCSV(String[] cells, PayVoucher voucher, Tutor tutor, HttpServletRequest req, HttpServletResponse resp) {
		
		// Get first and last name, as well as reformat date to avoid file path conflicts
		String[] name = tutor.getName().split(" ");
		String date = voucher.getDueDate().replace("/", "-");
		
		StringBuilder voucherInfo = new StringBuilder();
		
		// CSV headers
		voucherInfo.append("DATE");
		voucherInfo.append(',');
		voucherInfo.append("HOURS");
		voucherInfo.append(',');
		voucherInfo.append("WHAT_PERFORMED");
		voucherInfo.append(',');
		voucherInfo.append("WHERE_PERFORMED");
		voucherInfo.append('\n');
		
		// Add all cells to the CSV string
		for (int i = 0; i < cells.length; i += 4) {
			
			voucherInfo.append(cells[i]);
			voucherInfo.append(',');
			voucherInfo.append(cells[i + 1]);
			voucherInfo.append(',');
			voucherInfo.append(cells[i + 2]);
			voucherInfo.append(',');
			voucherInfo.append(cells[i + 3]);
			voucherInfo.append('\n');
		}		
		
		// Set and send the response
		try {

			resp.setContentType("text/csv");
			resp.setHeader("Content-Disposition","attachment; filename=" + name[1] + "_" + name[0] + "_" + date + "_Pay_Voucher.csv");
			
			OutputStream outputStream = resp.getOutputStream();
	        outputStream.write(voucherInfo.toString().getBytes());
	        outputStream.flush();
	        outputStream.close();
		} 
		catch (IOException e) {
			
			System.out.println(e.getMessage());
			req.setAttribute("errorMessage", "Export to CSV failed!");
		}
	}
}