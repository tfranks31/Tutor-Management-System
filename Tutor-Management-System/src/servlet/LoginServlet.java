package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.LoginController;
import model.Pair;
import model.Tutor;
import model.UserAccount;

public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private LoginController controller = null;
	private boolean forgotPassword = false;
	private boolean resetPassword = false;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Login Servlet: doGet");	
		
		forgotPassword = false;
		req.setAttribute("forgotPassword", forgotPassword);
		// Formally log out of the session
		req.getSession().setAttribute("user", null);

		req.setAttribute("forgotPassword", null);
		
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
		
		else if (req.getParameter("forgot") != null) {
			//if (!req.getParameter("forgot").equals("")) {
			
				forgotPassword = true;
				req.setAttribute("forgotPassword", forgotPassword);
				
				req.getRequestDispatcher("/_view/forgotPassword.jsp").forward(req, resp);
				
			//}
		}
		
		else if (req.getParameter("resetPassword") != null) {
			if (!req.getParameter("resetPassword").equals("")) {
		
				if (forgotPassword(req)) {
					
					req.setAttribute("errorMessage1", "");
				
					req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
				}
				
				else {
					
					req.getRequestDispatcher("/_view/forgotPassword.jsp").forward(req, resp);
				}
			}
		}
		
		else if (req.getParameter("return") != null) {
			if (!req.getParameter("return").equals("")) {
				
				req.setAttribute("errorMessage1", "");
				
				req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
			}
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
        
        if (user == null && req.getParameter("forgot") == null) {
        	req.setAttribute("errorMessage1", "Invalid username or password");
        	return false;
        } else if (user != null && req.getParameter("forgot") == null) {
        	req.getSession().setAttribute("user", user);
        	return true;
        } else {
        	return false;
        }
    }
    
    private boolean forgotPassword(HttpServletRequest req) {
    	
    	String username = req.getParameter("username");
    	String studentID = req.getParameter("studentID");
        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");
        
        Pair<UserAccount, Tutor> userTutor = controller.getUserTutorFromForgotPassword(username, studentID);
        
        if (userTutor == null) {
        	req.setAttribute("errorMessage2", "Incorrect user credentials");
        	return false;
        } 
        else if (!password1.equals(password2)) {
        	req.setAttribute("errorMessage2", "The passwords do not match");
        	return false;
        }
        else if (password1 == null || password1.equals("") || password2 == null || password2.equals("")) {
        	req.setAttribute("errorMessage2", "Please fill in both password fields");
        	return false;
        }
        else if (userTutor != null && password1.equals(password2) && password1 != null && !password1.equals("")) {
        	controller.updatePassword(userTutor.getLeft(), password1);
        	return true;
        }
        
        return false;

    }
}