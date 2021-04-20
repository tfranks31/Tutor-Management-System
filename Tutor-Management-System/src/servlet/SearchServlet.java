package servlet;

import java.io.IOException;
import java.util.ArrayList;

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
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Search Servlet: doGet");
		
		controller = new SearchController();
		
		UserAccount account = (UserAccount) req.getSession().getAttribute("user");
		
		
		// If user not logged in, redirect to login
		if (account == null) {
			
			resp.sendRedirect("login");
		}
		
		// User wants to add a tutor
		else if (req.getParameter("addTutor") != null) {
			
			//defaults to false
			req.getSession().setAttribute("edit", editTutor);
			
			resp.sendRedirect("addTutor");
		}
		
		// User wants to logout
		else if (req.getParameter("logout") != null) {
			resp.sendRedirect("login");
		}
		
		//Redirects to edit tutor page
		else if(req.getParameter("editTutor") != null && req.getParameter("editTutorName") != null) {
			//sets session variables
			boolean editTutor = true;
			String editName = (String) req.getParameter("editTutorName");
			System.out.println(editName);
			req.getSession().setAttribute("edit", editTutor);
			req.getSession().setAttribute("editName", editName);
			
			//redirects to page
			resp.sendRedirect("addTutor");
			
		}
		
		// Load search
		else {
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all Tutors and their vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
			
			// Filter out tutors and vouchers based on account info
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}
			
			if(tutorVoucherList.isEmpty()) {
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
			
			req.getRequestDispatcher("/payVoucher").forward(req, resp);
		}
		
		// User wants to assign a pay voucher
		else if (req.getParameter("assignVoucher") != null && req.getParameter("assign") != null) {
			
			String assignmentType = req.getParameter("assign");
			
			if (assignmentType.equals("allTutors")) {
				// Assign the voucher to all tutors
				controller.assignPayVoucherAll(req.getParameter("startDate"), req.getParameter("dueDate"));
			}
			if (assignmentType.equals("oneTutor")) {
				//assigns the voucher to one tutor
				controller.assignPayVoucherSpecific(req.getParameter("startDate"), req.getParameter("dueDate"), req.getParameter("assignName"));
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
			
			// Update search with the vouchers
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
		
		// User wants to search the pay vouchers with a search parameter
		else if (req.getParameter("search") != null) {
			
			controller = new SearchController();
			searchParameter = req.getParameter("search");
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all pay vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
			
			if (searchParameter == null) {
				//call all pay vouchers
				allTutorVoucherList = controller.getAllVouchers();
				if (allTutorVoucherList.isEmpty()) {
					req.setAttribute("errorMessage", "There were no pay vouchers found");
					System.out.println("Search Servlet: no Voucher Found");
				}
			}else {
				//call pay voucher per search
				allTutorVoucherList = controller.getVoucherFromSearch(searchParameter);
				if (allTutorVoucherList.isEmpty()) {
					req.setAttribute("errorMessage", "There were no pay vouchers found");
					System.out.println("Search Servlet: no Voucher Found");
				}
			}
			
			// Filter out tutors and vouchers based on account info
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}

			// Update search with the vouchers
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		} 
		
		// Default generate pay vouchers
		else {
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all pay vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
			
			// Filter out tutors and vouchers based on account info
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}
			
			// Update search with the vouchers
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
	}
}
