package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PayVoucherServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("AddTutor Servlet: doGet");	
		
		// Go back to search
		if (req.getParameter("back") != null) {
			
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
		
		// Load addTutor
		else {
			
			// Call JSP to generate empty form
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}
}
