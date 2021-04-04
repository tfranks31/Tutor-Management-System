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
			ArrayList<Pair<Tutor, PayVoucher>> tutorVoucherList = new ArrayList<Pair<Tutor, PayVoucher>>();
			ArrayList<Tutor> tutorList = new ArrayList<Tutor>();
			ArrayList<PayVoucher> voucherList = new ArrayList<PayVoucher>();
			
			searchParameter = req.getParameter("search");
			
			if (searchParameter == null) {
				//call all pay vouchers
				tutorVoucherList = controller.getAllVouchers();
			}else {
				//call pay voucher per search
				tutorVoucherList = controller.getVoucherFromSearch(searchParameter);
			}
			
			if(tutorVoucherList.isEmpty()) {
				System.out.println("Search Servlet: no Voucher Found");
			}
			
			for (Pair<Tutor, PayVoucher> tutorVoucher : tutorVoucherList) {
				tutorList.add(tutorVoucher.getLeft());
				voucherList.add(tutorVoucher.getRight());
			}
			
			req.setAttribute("payVouhcers", tutorVoucherList);
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
	}
}
