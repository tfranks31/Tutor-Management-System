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
	private String searchParameter = null;
	private String stillSearching = null;
	private boolean editTutor = false;
	private int pageNumber = 1;
	private String sort = null;
	
	boolean editProfile = false;
	boolean addTutor = false;
	int countName = 0;
	int countSubject = 0;
	int countDate = 0;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Search Servlet: doGet");
		
		controller = new SearchController();
		
		UserAccount account = (UserAccount) req.getSession().getAttribute("user");
		
		//sets all unused session variables and local counterparts to zero
		editTutor = false;
		req.getSession().setAttribute("edit", editTutor);
		editProfile = false;
		req.getSession().setAttribute("viewProfile", editProfile);
		addTutor = false;
		req.getSession().setAttribute("addTutor", addTutor);
		
		// If user not logged in, redirect to login
		
		if (account == null) {
			
			System.out.println("Search Servlet: nullUser");
			
			resp.sendRedirect("login");
		}
		// User wants to add a tutor
		else if (req.getParameter("addTutor") != null) {
			
			System.out.println("Search Servlet: addTutor");
			
			addTutor = true;
			req.getSession().setAttribute("addTutor", addTutor);
			
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
			
			ArrayList<Pair<Tutor, PayVoucher>> tempVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all Tutors and their vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers(null);
			// Filter out tutors and vouchers based on account info
	
			int count = 0;
			
			pageNumber = 1;
			req.getSession().setAttribute("pageNumber", pageNumber);
			
			if (account.getIsAdmin()) {
			
				for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
					
					if (count >= 7) {
						
						break;
					}
	
					
					if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
						
						tutorVoucherList.add(tutorVoucher);
					}
					
					count++;
				}
			}
			else {
				
				for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
					
					if (tutorVoucher.getLeft().getAccountID() == account.getAccountID()) {
						
						tempVoucherList.add(tutorVoucher);
					}
				}
				
				for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
					
					if ((tempVoucherList.size() - 1) <= i) {
						
						break;
					
					}
						
					tutorVoucherList.add(tempVoucherList.get(i));
				
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
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers(null);
			// Filter out tutors and vouchers based on account info
			for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
				
				if (allTutorVoucherList.get(i).getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(allTutorVoucherList.get(i));
				
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

			sort = req.getParameter("sort");
			if (sort != null) {
				if (sort.equals("Tutor Name")){
					countName ++;
					countSubject = 0;
					countDate = 0;
					System.out.println(countName % 3);
					if (countName % 3 == 2) {
						sort = "Name DESC";
					} else if(countName % 3 == 0) {
						sort = "Date";
					}
				}
				
				if (sort.equals("Subject")){
					countSubject ++;
					countName = 0;
					countDate = 0;
					if (countSubject % 3 == 2) {
						sort = "Subject DESC";
					} else if(countSubject % 3 == 0) {
						sort = "Date";
					}
				}
				
				if (sort.equals("Due Date")){
					countDate ++;
					countSubject = 0;
					countName = 0;
					if (countDate % 3 == 2) {
						sort = "Date ASC";
					} else if(countDate % 3 == 0) {
						sort = "Date";
					}
				}
			}
			System.out.println("Search Servlet: search");
			
			controller = new SearchController();
			searchParameter = req.getParameter("search");
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all pay vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getVoucherFromSearch(searchParameter,sort);
			// Filter out tutors and vouchers based on account info
			
			// Reset the page number to 1
			pageNumber = 1;
			req.getSession().setAttribute("pageNumber", pageNumber);
			
			for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
				
				if (allTutorVoucherList.size() <= i) {
					break;
				}
				
				tutorVoucherList.add(allTutorVoucherList.get(i));
			}

			if (tutorVoucherList.isEmpty()) {
				req.setAttribute("errorMessage", "There were no pay vouchers found");
				System.out.println("Search Servlet: no Voucher Found");
			}
			
			// Update search with the vouchers
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
			
			stillSearching = searchParameter;
			
			System.out.println(stillSearching);
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
		}
		
		else if(req.getParameter("page1") != null && pageNumber > 1) {
			if (!req.getParameter("page1").equals("")) {
				
				System.out.println("Search Servlet: Previous Page");
				
				pageNumber--;
				editProfile = false;
				req.getSession().setAttribute("viewProfile", editProfile);
				
				boolean editTutor = false;
				req.getSession().setAttribute("edit", editTutor);
				
				req.getSession().setAttribute("pageNumber", pageNumber);
			} 
			
			loadDefaultSearch(req, resp, account);	
		}
		
		else if (req.getParameter("page2") != null && (stillSearching == null || stillSearching.equals(""))) {
			if (!req.getParameter("page2").equals("") && account.getIsAdmin() && 
				((controller.getAllVouchers(null).size() % 7 != 0 && (controller.getAllVouchers(null).size() / 7) + 1 > pageNumber)) ||
				(controller.getAllVouchers(null).size() % 7 == 0 && (controller.getAllVouchers(null).size() / 7) > pageNumber)) {
			
				System.out.println("Search Servlet: Next Page");
				
				pageNumber++;
				
				req.getSession().setAttribute("pageNumber", pageNumber);
				
			}
			
			loadDefaultSearch(req, resp, account);	
		}
		
		else if (req.getParameter("page2") != null && stillSearching != null) {
				if (!req.getParameter("page2").equals("") && account.getIsAdmin() && !stillSearching.equals("") && 
				((controller.getVoucherFromSearch(stillSearching,null).size() % 7 != 0 && (controller.getVoucherFromSearch(stillSearching,null).size() / 7) + 1 > pageNumber)) ||
				(controller.getVoucherFromSearch(stillSearching,null).size() % 7 == 0 && (controller.getVoucherFromSearch(stillSearching,null).size() / 7) > pageNumber)) {
					System.out.println("Search Servlet: Next Page");
					
					pageNumber++;
					
					req.getSession().setAttribute("pageNumber", pageNumber);
					
					System.out.println(stillSearching);
				}
			
			loadDefaultSearch(req, resp, account);	
		}
		
		else if (req.getParameter("page2") != null && !req.getParameter("page2").equals("")
				 && !account.getIsAdmin()) {
			
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers(null);
			
			ArrayList<Pair<Tutor, PayVoucher>> tempVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID()) {
					
					tempVoucherList.add(tutorVoucher);
				}
			}
			
			if ((tempVoucherList.size() / 7) + 1 > pageNumber) {
				System.out.println("Search Servlet: Next Page");
				
				pageNumber++;
				
				req.getSession().setAttribute("pageNumber", pageNumber);
				
			}
			
			loadDefaultSearch(req, resp, account);
		}
		
		else if (req.getParameter("sort") != null) {
			sort = req.getParameter("sort");
			if (sort.equals("Tutor Name")){
				countName ++;
				countSubject = 0;
				countDate = 0;
				System.out.println(countName % 3);
				if (countName % 3 == 2) {
					sort = "Name DESC";
				} else if(countName % 3 == 0) {
					sort = "Date";
				}
			}
			
			if (sort.equals("Subject")){
				countSubject ++;
				countName = 0;
				countDate = 0;
				if (countSubject % 3 == 2) {
					sort = "Subject DESC";
				} else if(countSubject % 3 == 0) {
					sort = "Date";
				}
			}
			
			if (sort.equals("Due Date")){
				countDate ++;
				countSubject = 0;
				countName = 0;
				if (countDate % 3 == 2) {
					sort = "Date ASC";
				} else if(countDate % 3 == 0) {
					sort = "Date";
				}
			}
			loadDefaultSearch(req, resp, account);
		}
		
		// Default generate pay vouchers
		else {
			loadDefaultSearch(req, resp, account);
;		}
	}
	
	// Default generate pay vouchers
	private void loadDefaultSearch(HttpServletRequest req, HttpServletResponse resp, UserAccount account)
			throws ServletException, IOException {
		ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
		
		ArrayList<Pair<Tutor, PayVoucher>> tempVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
		
		// Get all pay vouchers
		ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList;
		
		if (stillSearching == null) {
			allTutorVoucherList = controller.getAllVouchers(sort);
		}
		else {
			allTutorVoucherList = controller.getVoucherFromSearch(stillSearching, sort);
		}
		
		if (account.getIsAdmin()) {
			// Filter out tutors and vouchers based on account info
			if (stillSearching == null) {
				for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
					
					if ((allTutorVoucherList.size()) <= i) {
						
						break;
					
					}
						
					tutorVoucherList.add(allTutorVoucherList.get(i));
				
				}	
			}
			else  {

				for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
						
						tempVoucherList.add(tutorVoucher);
					
				}
				
				for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
					
					if ((tempVoucherList.size()) <= i) {
						
						break;
					
					}
						
					tutorVoucherList.add(tempVoucherList.get(i));
				
				}	
			}
		}
		
		else  {
			
			// Filter out tutors and vouchers based on account info
			
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID()) {
					
					tempVoucherList.add(tutorVoucher);
				}
			}
			
			for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
				
				if ((tempVoucherList.size() - 1) <= i) {
					
					break;
				
				}
					
				tutorVoucherList.add(tempVoucherList.get(i));
			
			}
		}
	
		
		if (tutorVoucherList.isEmpty()) {
			req.setAttribute("errorMessage", "There were no pay vouchers found");
			System.out.println("Search Servlet: no Voucher Found");
		}
		
		System.out.print("Ethan " + stillSearching + "Ethan");
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
