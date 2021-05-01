package servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.SearchController;
import model.Pair;
import model.PayVoucher;
import model.Tutor;
import model.UserAccount;



public class SearchServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private SearchController controller = null;
	String searchParameter = null;
	boolean editTutor = false;
	boolean editProfile = false;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Search Servlet: doGet");
		
		controller = new SearchController();
		
		UserAccount account = (UserAccount) req.getSession().getAttribute("user");
		
		
		// If user not logged in, redirect to login
		if (account == null) {
			
			System.out.println("Search Servlet: nullUser");
			
			resp.sendRedirect("login");
		}
		
		// User wants to add a tutor
		else if (req.getParameter("addTutor") != null) {
			
			System.out.println("Search Servlet: addTutor");
			
			//defaults to false
			req.getSession().setAttribute("edit", editTutor);
			req.getSession().setAttribute("viewProfile", editProfile);
			resp.sendRedirect("addTutor");
		}
		
		// User wants to logout
		else if (req.getParameter("logout") != null) {
			
			System.out.println("Search Servlet: logout");
			
			resp.sendRedirect("login");
		}
		
		// Load search
		else {
			
			System.out.println("Search Servlet: loadSearch");
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all Tutors and their vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
			
			// Filter out tutors and vouchers based on account info
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}
			
			if (tutorVoucherList.isEmpty()) {
				req.setAttribute("errorMessage", "There were no pay vouchers found");
				System.out.println("Search Servlet: no Voucher Found");
			}
			
			// Update search with the tutors and vouchers
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Search Servlet: doPost");	
		
		controller = new SearchController();
		
		UserAccount account = (UserAccount) req.getSession().getAttribute("user");
		// User selected to view a pay voucher
		if (req.getParameter("ID") != null) {
			
			System.out.println("Search Servlet: load Voucher");
			
			req.getRequestDispatcher("/payVoucher").forward(req, resp);
		}
		
		// User wants to assign a pay voucher
		else if (req.getParameter("assignVoucher") != null && req.getParameter("assign") != null) {
			
			String assignmentType = req.getParameter("assign");
			
			if (assignmentType.equals("allTutors")) {
				
				System.out.println("Search Servlet: assign voucher all");
				
				if (searchValidate(req)) {
					
					// Assign the voucher to all tutors
					controller.assignPayVoucherAll(req.getParameter("startDate"), req.getParameter("dueDate"));
				}			
			}
			if (assignmentType.equals("oneTutor")) {
				
				System.out.println("Search Servlet: assign voucher specific");
				
				if (searchValidate(req)) {
					
					//assigns the voucher to one tutor
					controller.assignPayVoucherSpecific(req.getParameter("startDate"), req.getParameter("dueDate"), req.getParameter("assignName"));
				}				
			}
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all tutors and their vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
			
			// Filter out tutors and vouchers based on account info
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}
			
			if (tutorVoucherList.isEmpty()) {
				req.setAttribute("errorMessage", "There were no pay vouchers found");
				System.out.println("Search Servlet: no Voucher Found");
			}
			
			// Update search with the vouchers
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
		
		// User wants to search the pay vouchers with a search parameter
		else if (req.getParameter("search") != null) {
			
			System.out.println("Search Servlet: search");
			
			controller = new SearchController();
			searchParameter = req.getParameter("search");
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all pay vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getVoucherFromSearch(searchParameter);
			
			// Filter out tutors and vouchers based on account info
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}

			if (tutorVoucherList.isEmpty()) {
				req.setAttribute("errorMessage", "There were no pay vouchers found");
				System.out.println("Search Servlet: no Voucher Found");
			}

			// Update search with the vouchers
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		} 
		
		//Redirects to edit tutor page
		else if(req.getParameter("editTutor") != null && !req.getParameter("editTutorName").equals("")) {
			//sets session variables
			
			System.out.println("Search Servlet: edit tutor");
			
			boolean editTutor = true;
			String editName = (String) req.getParameter("editTutorName");
			
			Pair <UserAccount, Tutor> userTutorPair = controller.getTutorInfo(editName);
		
			if (userTutorPair != null) {
				req.getSession().setAttribute("edit", editTutor);
				req.getSession().setAttribute("editUser", userTutorPair.getLeft());
				req.getSession().setAttribute("editTutor", userTutorPair.getRight());
				//req.getSession().setAttribute("editName", editName);
				
				editProfile = true;
				req.getSession().setAttribute("viewProfile", editProfile);
				
				//redirects to page
				resp.sendRedirect("addTutor");
				
			}
			
			//loads the default search page
			else {
				loadDefaultSearch(req, resp, account);
			}
		}else if (req.getParameter("tutorProfile") != null){
			Tutor tutor = controller.getTutorByUserID(account);
			
			boolean editProfile = true;
			req.getSession().setAttribute("viewProfile", editProfile);
			req.getSession().setAttribute("tutorProfileInfo", tutor);
			
			editProfile = false;
			req.getSession().setAttribute("viewProfile", editProfile);
			
			boolean editTutor = false;
			req.getSession().setAttribute("edit", editTutor);
			
			//redirects to page
			resp.sendRedirect("addTutor");
		}
		// Default generate pay vouchers
		else {
			loadDefaultSearch(req, resp, account);
		}
	}
	
	// Default generate pay vouchers
	private void loadDefaultSearch(HttpServletRequest req, HttpServletResponse resp, UserAccount account)
			throws ServletException, IOException {
		ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
		
		// Get all pay vouchers
		ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
		
		// Filter out tutors and vouchers based on account info
		for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
			
			if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
				
				tutorVoucherList.add(tutorVoucher);
			}
		}
		
		if (tutorVoucherList.isEmpty()) {
			req.setAttribute("errorMessage", "There were no pay vouchers found");
			System.out.println("Search Servlet: no Voucher Found");
		}
		
		// Update search with the vouchers
		req.setAttribute("payVouchers", tutorVoucherList);
		req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
	}
	
	private boolean searchValidate(HttpServletRequest req) {
		
		String startDate = req.getParameter("startDate");
		String dueDate = req.getParameter("dueDate");
		
		// Check for valid date format
		if (!startDate.matches("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$")
			|| !dueDate.matches("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$")) {
			
			req.setAttribute("assignErrorMessage", "Please enter a valid date");
			return false;
		}
		
		try {
			
			Date start = new SimpleDateFormat("MM/dd/yyyy").parse(startDate);
			Date due = new SimpleDateFormat("MM/dd/yyyy").parse(dueDate);
			
			if (start.after(due)) {
				
				req.setAttribute("assignErrorMessage", "The due date must start after the start date");
				return false;
			}
		} 
		catch (ParseException e) {

			return false;
		}
				
		return true;
	}
}
