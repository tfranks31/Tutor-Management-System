package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.LoginController;
import model.UserAccount;

public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private LoginController controller = null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Login Servlet: doGet");	
		
		// Formally log out of the session
		req.getSession().setAttribute("user", null);
		
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Login Servlet: doPost");
		
        controller = new LoginController();
		
		// Go to search if the user has logged in
		if (loginValidate(req)) {
			
			System.out.println("Login Servlet: login sucessful");
			
			resp.sendRedirect("search");
		}
		// Reload login
		else {
			// Call JSP to generate empty form
			
			System.out.println("Login Servlet: loginFailed");
			
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
		}
		
	}
    
    private boolean loginValidate(HttpServletRequest req) {
    	
    	// Validate login information and retrieve the account
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        UserAccount user = controller.getUserFromLogin(username, password);
        
        if (user == null) {
        	req.setAttribute("errorMessage", "Invalid username or password");
        	return false;
        } else {
        	req.getSession().setAttribute("user", user);
        	return true;
        }
    }
}