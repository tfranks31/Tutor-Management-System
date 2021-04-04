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
	private String username;
	private String password;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Login Servlet: doGet");	
		
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Login Servlet: doPost");	
		
		// Go to search if the user has logged in
		if (loginValidate(req)) {
			resp.sendRedirect("search");
		}
		// Load addTutor
		else {
			// Call JSP to generate empty form
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
		}
		
	}
    
    private boolean loginValidate(HttpServletRequest req) {
        username = req.getParameter("username");
        password = req.getParameter("password");
        
        controller = new LoginController();
        UserAccount user = controller.getUserFromLogin(username, password);
        
        if (user == null) {
        	return false;
        }else {
        	return true;
        }
    }
}