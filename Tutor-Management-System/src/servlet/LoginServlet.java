package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PayVoucherServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Login Servlet: doPost");	
		
		// Go to search if the user has logged in
		if (loginValidate(req))
			req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
		}
		// Load addTutor
		else {
			// Call JSP to generate empty form
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}
    
    private boolean loginValidate(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username.equals("nickelback") && password.equals("nickelback")) {
            return true;
        } else {
            return false;
        }
    }
}