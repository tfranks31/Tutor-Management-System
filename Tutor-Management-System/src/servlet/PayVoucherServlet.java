package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PayVoucherServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	int tableSize;
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
			tableSize = 10;
			req.setAttribute("tableSize", tableSize);
			// Call JSP to generate empty form
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("PayVoucher Servlet: doPost");	

		// Go back to search
		if (req.getParameter("addRow") != null) {	
			tableSize += 1;
			if(tableSize > 15) {
				tableSize = 15;
			}
			req.setAttribute("tableSize", tableSize);
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}

}
