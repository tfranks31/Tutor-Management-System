package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.SearchController;
import model.PayVoucher;

public class SearchServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private SearchController controller = null;
	String searchParameter = null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Search Servlet: doGet");
		
		if (req.getParameter("addTutor") != null) {
			
			resp.sendRedirect("addTutor");
		}
		else {
			controller = new SearchController();
			ArrayList<PayVoucher> voucherList = new ArrayList<PayVoucher>();
			
			searchParameter = req.getParameter("search");
			
			if (searchParameter == null) {
				//call all pay vouchers
				voucherList = controller.getAllVouchers();
			}else {
				//call pay voucher per search
				voucherList = controller.getVoucherFromSearch(searchParameter);
			}
			
			if(voucherList.isEmpty()) {
				System.out.println("Search Servlet: no Voucher Found");
			}
			
			req.setAttribute("payVouhcers", voucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
	}
}
