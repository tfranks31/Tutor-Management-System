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
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Search Servlet: doGet");
		
		UserAccount account = (UserAccount) req.getSession().getAttribute("user");
		if (account == null) {
			
			resp.sendRedirect("login");
			return;
		}
		
		if (req.getParameter("addTutor") != null) {
			
			resp.sendRedirect("addTutor");
		}
		else {
			controller = new SearchController();
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
			
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}
			
			if(tutorVoucherList.isEmpty()) {
				
				System.out.println("Search Servlet: no Voucher Found");
			}
			
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
		
		if (req.getParameter("ID") != null) {
			
			req.getRequestDispatcher("/payVoucher").forward(req, resp);
		}
		else if (req.getParameter("assignVoucher") != null) {
			
			controller.assignPayVoucher(req.getParameter("startDate"), req.getParameter("dueDate"));
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
			
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}
			req.setAttribute("payVouchers", tutorVoucherList);
			
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
		else if (req.getParameter("search") != null) {
			
			controller = new SearchController();
			searchParameter = req.getParameter("search");
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
			
			if (searchParameter == null) {
				//call all pay vouchers
				allTutorVoucherList = controller.getAllVouchers();
			}else {
				//call pay voucher per search
				allTutorVoucherList = controller.getVoucherFromSearch(searchParameter);
			}
			
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}
			
			if(tutorVoucherList.isEmpty()) {
				System.out.println("Search Servlet: no Voucher Found");
			}
					
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
		else {
			
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			ArrayList<Pair<Tutor, PayVoucher>> allTutorVoucherList = controller.getAllVouchers();
			
			for (Pair<Tutor, PayVoucher> tutorVoucher : allTutorVoucherList) {
				
				if (tutorVoucher.getLeft().getAccountID() == account.getAccountID() || account.getIsAdmin()) {
					
					tutorVoucherList.add(tutorVoucher);
				}
			}
			req.setAttribute("payVouchers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
	}
}
