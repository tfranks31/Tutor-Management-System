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
	String searchParameterTutor = null;
	String stillSearching = null;
	String stillSearchingTutor = null;
	boolean editTutor = false;
	boolean editProfile = false;
	boolean addTutor = false;
	
	private int pageNumber = 1;
	private int pageNumberTutor = 1;
	
	private String sort = null;
	int countName = 0;
	int countSubject = 0;
	int countDate = 0;
	
	
	
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
		
		// User wants to go to the tutor page, only admins can
		else if ((editTutor ||  addTutor) && account.getIsAdmin()) {

			//sets all unused session variables and local counterparts to zero
			editTutor = false;
			req.getSession().setAttribute("edit", editTutor);
			
			editProfile = false;
			req.getSession().setAttribute("viewProfile", editProfile);
			
			addTutor = false;
			req.getSession().setAttribute("addTutor", addTutor);
			
			
			loadDefaultTutorPage(req, resp);
			
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
			
			//sets all unused session variables and local counterparts to zero
			editTutor = false;
			req.getSession().setAttribute("edit", editTutor);
			
			editProfile = false;
			req.getSession().setAttribute("viewProfile", editProfile);
			
			addTutor = false;
			req.getSession().setAttribute("addTutor", addTutor);
			
			
			System.out.println("Search Servlet: loadSearch");
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			ArrayList<Pair<Tutor, PayVoucher>> tempVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all Tutors and their vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers(null);
			// Filter out tutors and vouchers based on account info
	
			int count = 0;
			
			pageNumber = 1;
			req.getSession().setAttribute("pageNumber", pageNumber);
			
			// Load all pay vouchers if an admin is logged in
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
			
			// Load only the pay vouchers that the tutor is allowed to see
			else {
				
				for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
					
					if (tutorVoucher.getLeft().getAccountID() == account.getAccountID()) {
						
						tempVoucherList.add(tutorVoucher);
					}
				}
				
				for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
					
					if ((tempVoucherList.size()) <= i) {
						
						break;
					
					}
						
					tutorVoucherList.add(tempVoucherList.get(i));
				
				}
			}
			
			// No vouchers were found
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
		//user wants to view profile
		else if (req.getParameter("tutorProfile") != null){
			Tutor tutor = controller.getTutorByUserID(account);
			
			boolean editProfile = true;
			req.getSession().setAttribute("viewProfile", editProfile);
			req.getSession().setAttribute("tutorProfileInfo", tutor);
			
			//redirects to page
			resp.sendRedirect("addTutor");
		} 
		//Redirects to edit tutor page
		else if (req.getParameter("userTutorID") != null) {
			
			System.out.println("Search Servlet: Edit Tutor");
			
			System.out.println("UserTutorID: " + req.getParameter("userTutorID"));
			
				System.out.println("debug: " + req.getParameter("assignVoucher"));
			editTutor = true;
			req.getSession().setAttribute("edit", editTutor);
			
			int userID =  Integer.parseInt(req.getParameter("userTutorID"));
			
			Pair<UserAccount, Tutor> userTutorPair = controller.getUserTutorByAccountID(userID);
			
			req.getSession().setAttribute("editUser", userTutorPair.getLeft());
			req.getSession().setAttribute("editTutor", userTutorPair.getRight());
			
			resp.sendRedirect("addTutor");
		}
		
		// User wants to assign a pay voucher
		else if (req.getParameter("assignVoucher") != null && req.getParameter("assign") != null) {
			
			String assignmentType = req.getParameter("assign");
			
			// Assign to all tutors
			if (assignmentType.equals("allTutors")) {
				
				System.out.println("Search Servlet: assign voucher all");
				
				if (searchValidate(req)) {
					req.setAttribute("voucherAdded", "Pay Vouchers were added Sucessfully");
					// Assign the voucher to all tutors
					controller.assignPayVoucherAll(req.getParameter("startDate"), req.getParameter("dueDate"));
				}			
			}
			
			// Assign to a specific tutor
			if (assignmentType.equals("oneTutor")) {
				
				System.out.println("Search Servlet: assign voucher specific");
				
				
				if (searchValidate(req)) {
					req.setAttribute("voucherAdded", "Pay Voucher was added Sucessfully");
					//assigns the voucher to one tutor
					controller.assignPayVoucherSpecific(req.getParameter("startDate"), req.getParameter("dueDate"),  req.getParameter("assignUserName"));
				}				
			}
			
			ArrayList<Pair<UserAccount, Tutor>> userTutorList = new ArrayList<Pair<UserAccount, Tutor>>();
			
			// Get all tutors and their vouchers
			ArrayList<Pair<UserAccount, Tutor>> allUserTutorList = controller.getAllUserTutors();
			// Filter out tutors and vouchers based on account info
			for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
				
				if (allUserTutorList.size() <= i) {
					break;
				}
				
				if (allUserTutorList.get(i).getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					userTutorList.add(allUserTutorList.get(i));
				
				}
			}
			
			// No tutors were found
			if (userTutorList.isEmpty()) {
				req.setAttribute("errorMessage", "There were no tutors found");
				System.out.println("Search Servlet: no tutors Found");
			}
			
			// Update search with the vouchers
			req.setAttribute("UserTutors", userTutorList);
			req.getRequestDispatcher("/_view/viewTutors.jsp").forward(req, resp);
		}
		
		// User wants to search the pay vouchers with a search parameter
		else if (req.getParameter("search") != null) {

			sort = req.getParameter("sort");
			
			// User has an applied sort
			if (sort != null) {
				
				// Sort by tutor name, count to account for sort directions
				if (sort.equals("Tutor Name")){
					countName ++;
					countSubject = 0;
					countDate = 0;
					if (countName % 3 == 2) {
						sort = "Name DESC";
					} else if(countName % 3 == 0) {
						sort = "Date";
					}
				}
				
				// Sort by subject, count to account for sort directions
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
				
				// Sort by due date, count to account for sort directions
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
			
			ArrayList<Pair<Tutor, PayVoucher>> tempVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			// Get all pay vouchers
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getVoucherFromSearch(searchParameter,sort);
			// Filter out tutors and vouchers based on account info
			
			// Reset the page number to 1
			pageNumber = 1;
			req.getSession().setAttribute("pageNumber", pageNumber);
			
			// Load all pay vouchers if the user is an admin
			if (account.getIsAdmin()) {
				
				for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
					
					if (allTutorVoucherList.size() <= i) {
						break;
					}
					
					tutorVoucherList.add(allTutorVoucherList.get(i));
				}
			}
			
			// Load only the pay vouchers assigned to the logged in tutor
			else {
				
				for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
					
					if (tutorVoucher.getLeft().getAccountID() == account.getAccountID()) {
						
						tempVoucherList.add(tutorVoucher);
					}
				
				}
			
				for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
				
					if ((tempVoucherList.size()) <= i) {
					
						break;
				
					}
					
				tutorVoucherList.add(tempVoucherList.get(i));
				}
			}

			// No pay vouchers were found
			if (tutorVoucherList.isEmpty()) {
				req.setAttribute("errorMessage", "There were no pay vouchers found");
				System.out.println("Search Servlet: no Voucher Found");
			}
			
			// Update search with the vouchers
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
			
			stillSearching = searchParameter;
			
		}
		
		// User wants to search for a tutor
		else if (req.getParameter("tutorSearch") != null) {
			
			System.out.println("Search Servlet: tutor search");
			
			controller = new SearchController();
			searchParameterTutor = req.getParameter("tutorSearch");	

			ArrayList<Pair<UserAccount, Tutor>> allUserTutors = controller.getUserTutorsFromSearch(searchParameterTutor);
			
			ArrayList<Pair<UserAccount, Tutor>> tempUserTutor = new ArrayList<Pair<UserAccount, Tutor>>();
			
			ArrayList<Pair<UserAccount, Tutor>> userTutorList =  new ArrayList<Pair<UserAccount, Tutor>>();
			
			pageNumberTutor = 1;
			req.getSession().setAttribute("pageNumberTutor", pageNumberTutor);
			
			// Load all tutors
			if (account.getIsAdmin()) {
				
				for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
					
					if (allUserTutors.size() <= i) {
						
						break;
					}
					
					userTutorList.add(allUserTutors.get(i));
				}
			}
			
			else {
				
				for (Pair<UserAccount, Tutor> userTutor : allUserTutors) {
					
					if (userTutor.getLeft().getAccountID() == account.getAccountID()) {
						
						tempUserTutor.add(userTutor);
					}
				
				}
			
				for (int i = ((pageNumber - 1) * 7); i < (pageNumber * 7); i++) {
				
					if ((tempUserTutor.size()) <= i) {
					
						break;
				
					}
					
				userTutorList.add(tempUserTutor.get(i));
				}
			}
			
			// No tutors were found
			if (userTutorList.isEmpty()) {
				req.setAttribute("errorMessage", "There were no Tutors found");
				System.out.println("Search Servlet: no Tutors Found");
			}

			// Reset the page number to 1
			req.getSession().setAttribute("pageNumberTutor", 1);
			pageNumberTutor = 1;
			
			// Update search with the vouchers
			req.setAttribute("UserTutors", userTutorList);
			req.getRequestDispatcher("/_view/viewTutors.jsp").forward(req, resp);
			
			stillSearchingTutor = searchParameterTutor;
		} 
		
		//admin wants to view tutor page
		else if (req.getParameter("tutorPage") != null){
			
			pageNumberTutor = 1;
			req.getSession().setAttribute("pageNumberTutor", pageNumberTutor);
			
			loadDefaultTutorPage(req, resp);
		}
		
		//Admin clicked back button on tutor page
		else if (req.getParameter("tutorPageBack") != null){
		
			loadDefaultSearch(req,resp, account);
		}
		
		// User wants to go back a page in voucher search
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
		
		// User wants to go back a page in tutor search
		else if(req.getParameter("page3") != null && pageNumberTutor > 1) {
			if (!req.getParameter("page3").equals("")) {
				
				System.out.println("Search Servlet: Previous Page");
				
				pageNumberTutor--;
				editProfile = false;
				req.getSession().setAttribute("viewProfile", editProfile);
				
				boolean editTutor = false;
				req.getSession().setAttribute("edit", editTutor);
				
				req.getSession().setAttribute("pageNumberTutor", pageNumberTutor);
				
			}	
			
			loadDefaultTutorPage(req, resp);
		}
		
		// User wants to go back a page but keep them on the same page
		else if(req.getParameter("page3") != null && pageNumberTutor == 1) {
			if (!req.getParameter("page3").equals("")) {
				
				loadDefaultTutorPage(req, resp);
			}
		}
		
		// User wants to go to the next page in voucher search
		else if (req.getParameter("page2") != null && (stillSearching == null || stillSearching.equals("")) && account.getIsAdmin()) {
			if (!req.getParameter("page2").equals("") && 
				((controller.getAllVouchers(null).size() % 7 != 0 && (controller.getAllVouchers(null).size() / 7) + 1 > pageNumber)) ||
				(controller.getAllVouchers(null).size() % 7 == 0 && (controller.getAllVouchers(null).size() / 7) > pageNumber)) {
			
				System.out.println("Search Servlet: Next Page");
				
				pageNumber++;
				
				req.getSession().setAttribute("pageNumber", pageNumber);
				
			}
			
			loadDefaultSearch(req, resp, account);	
		}
		
		// User wants to go to the next page in tutor search
		else if (req.getParameter("page4") != null && (stillSearchingTutor == null || stillSearchingTutor.equals("")) && account.getIsAdmin()) {
			if (!req.getParameter("page4").equals("") && 
				((controller.getAllUserTutors().size() % 7 != 0 && (controller.getAllUserTutors().size() / 7) + 1 > pageNumberTutor)) ||
				(controller.getAllUserTutors().size() % 7 == 0 && (controller.getAllUserTutors().size() / 7) > pageNumberTutor)) {
			
				System.out.println("Search Servlet: Next Page");
				
				pageNumberTutor++;
				
				req.getSession().setAttribute("pageNumberTutor", pageNumberTutor);
				
			}
			
			loadDefaultTutorPage(req, resp);
		}
		
		// User wants to go to the next page but they are still searching for something
		else if (req.getParameter("page2") != null && stillSearching != null && account.getIsAdmin()) {
				if (!req.getParameter("page2").equals("") && !stillSearching.equals("") && 
				((controller.getVoucherFromSearch(stillSearching,null).size() % 7 != 0 && (controller.getVoucherFromSearch(stillSearching,null).size() / 7) + 1 > pageNumber)) ||
				(controller.getVoucherFromSearch(stillSearching,null).size() % 7 == 0 && (controller.getVoucherFromSearch(stillSearching,null).size() / 7) > pageNumber)) {
					System.out.println("Search Servlet: Next Page");
					
					pageNumber++;
					
					req.getSession().setAttribute("pageNumber", pageNumber);
					

				}
			
			loadDefaultSearch(req, resp, account);	
		}
		
		// User wants to go to the next page but they are still searching for a tutor
		else if (req.getParameter("page4") != null && stillSearchingTutor != null && account.getIsAdmin()) {
			if (!req.getParameter("page4").equals("") && !stillSearchingTutor.equals("") && 
			((controller.getUserTutorsFromSearch(stillSearchingTutor).size() % 7 != 0 && (controller.getUserTutorsFromSearch(stillSearchingTutor).size() / 7) + 1 > pageNumber)) ||
			(controller.getUserTutorsFromSearch(stillSearchingTutor).size() % 7 == 0 && (controller.getUserTutorsFromSearch(stillSearchingTutor).size() / 7) > pageNumber)) {
				System.out.println("Search Servlet: Next Page");
				
				pageNumberTutor++;
				
				req.getSession().setAttribute("pageNumberTutor", pageNumberTutor);
				

			}
		
			loadDefaultTutorPage(req, resp);
		}
		
		else if (req.getParameter("page2") != null && !account.getIsAdmin() &&
				(stillSearching == null || stillSearching.equals(""))) {
			
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers(null);
			
			ArrayList<Pair<Tutor, PayVoucher>> tempVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID()) {
					
					tempVoucherList.add(tutorVoucher);
				}
			}
			
			if (!req.getParameter("page2").equals("") &&
			   ((tempVoucherList.size() % 7 != 0 && tempVoucherList.size() / 7 + 1 > pageNumber) ||
			   (tempVoucherList.size() % 7 == 0 && tempVoucherList.size() / 7 > pageNumber)))	{
				
				System.out.println("Search Servlet: Next Page");
				
				pageNumber++;
				
				req.getSession().setAttribute("pageNumber", pageNumber);
				
			}
			
			System.out.println("Is here");
			
			loadDefaultSearch(req, resp, account);
		}
		
		// Tutor wants to go to the next page and is still searching
		else if (req.getParameter("page2") != null && !account.getIsAdmin() &&
				(stillSearching != null || !stillSearching.equals(""))) {
			
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getVoucherFromSearch(stillSearching, null);
			
			ArrayList<Pair<Tutor, PayVoucher>> tempVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID()) {
					
					tempVoucherList.add(tutorVoucher);
				}
			}
			
			if (!req.getParameter("page2").equals("") &&
			   ((tempVoucherList.size() % 7 != 0 && tempVoucherList.size() / 7 + 1 > pageNumber) ||
			   (tempVoucherList.size() % 7 == 0 && tempVoucherList.size() / 7 > pageNumber)))	{
				
				System.out.println("Search Servlet: Next Page");
				
				pageNumber++;
				
				req.getSession().setAttribute("pageNumber", pageNumber);
				
			}
			
			
			loadDefaultSearch(req, resp, account);
		}
		
		// User has an applied sort
		else if (req.getParameter("sort") != null) {
			sort = req.getParameter("sort");
			
			// User wants to sort by Tutor Name
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
			
			// User wants to sort by subject
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
			
			// User wants to sort by due date
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
		}
	}
	
	/**
	 * Load a default search for pay vouchers.
	 * @param req Request to set attributes and forward.
	 * @param resp Response to forward.
	 * @param account The active user account.
	 * @throws ServletException
	 * @throws IOException
	 */
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
				
				if ((tempVoucherList.size()) <= i) {
					
					break;
				
				}
					
				tutorVoucherList.add(tempVoucherList.get(i));
			
			}
		}
	
		// No pay vouchers were found
		if (tutorVoucherList.isEmpty()) {
			req.setAttribute("errorMessage", "There were no pay vouchers found");
			System.out.println("Search Servlet: no Voucher Found");
		}
		
		// Update search with the vouchers
		req.setAttribute("payVouchers", tutorVoucherList);
		req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
	}
	
	/**
	 * Validate the inputed search parameters.
	 * @param req Request that holds the information to validate.
	 * @return True if the search is valid, false if it is not.
	 */
	private boolean searchValidate(HttpServletRequest req) {
		
		String startDate = req.getParameter("startDate");
		String dueDate = req.getParameter("dueDate");
		System.out.println(startDate);
		System.out.println(dueDate);
		// Check for valid date format
		if (!startDate.matches("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$")
			|| !dueDate.matches("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$")) {
			
			req.setAttribute("assignErrorMessage", "Please enter a valid date");
			return false;
		}
		
		// Check that due date is not before start date
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
	
	/**
	 * Load the default tutor page.
	 * @param req Request to set attributes and forward.
	 * @param resp Response to forward.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void loadDefaultTutorPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Search Servelet: load tutor page ");
		
		ArrayList<Pair<UserAccount, Tutor>> allUserTutorList = new ArrayList<Pair<UserAccount, Tutor>>();
		
		ArrayList<Pair<UserAccount, Tutor>> userTutors = new ArrayList<Pair<UserAccount, Tutor>>();
		
		ArrayList<Pair<UserAccount, Tutor>> tempUserTutors = new ArrayList<Pair<UserAccount, Tutor>>();
		
		if (stillSearchingTutor == null) {
			
			userTutors =  controller.getAllUserTutors();
			
			for (int i = ((pageNumberTutor - 1) * 7); i < (pageNumberTutor * 7); i++) {
				
				if (userTutors.size() <= i) {
					
					break;
				}	
				
				allUserTutorList.add(userTutors.get(i));
			}	
		}
		
		else {
			userTutors =  controller.getUserTutorsFromSearch(stillSearchingTutor);
			 
			for (Pair<UserAccount, Tutor> userTutor : userTutors) {
				
				tempUserTutors.add(userTutor);
			}
			
			for (int i = ((pageNumberTutor - 1) * 7); i < (pageNumberTutor * 7); i++) {
				
				if ((tempUserTutors.size() <= i)) {
					
					break;
				}
				
				allUserTutorList.add(tempUserTutors.get(i));
			}
		}
		
	
		// No tutors were found
		if (allUserTutorList.isEmpty()) {
			req.setAttribute("errorMessage", "There were no Tutors found");
			System.out.println("Search Servlet: no Tutors Found Found");
		}
		
		// Update search with the tutors
		req.setAttribute("UserTutors", allUserTutorList);;
		req.getRequestDispatcher("/_view/viewTutors.jsp").forward(req, resp);
	}
}