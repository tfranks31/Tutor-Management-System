package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddTutorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("AddTutor Servlet: doGet");	
		
		// Go back to search
		if (req.getParameter("back") != null) {
			
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
		// Load addTutor
		else {
			
			// Call JSP to generate empty form
			req.getRequestDispatcher("/_view/addTutor.jsp").forward(req, resp);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("AddTutor Servlet: doPost");
		
		// If the tutor information is valid, continue to the search page
		if (tutorValidate(req)) {
			
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
		// If the tutor information is invalid, set all parameters with inputed
		// parameters and reload the add tutor page with proper error message
		else {
			
			req.setAttribute("firstName", req.getParameter("firstName"));
			req.setAttribute("lastName", req.getParameter("lastName"));
			req.setAttribute("username", req.getParameter("username"));
			req.setAttribute("password", req.getParameter("password"));
			req.setAttribute("email", req.getParameter("email"));
			req.setAttribute("accountNumber", req.getParameter("accountNumber"));
			req.setAttribute("subject", req.getParameter("subject"));
			req.setAttribute("payRate", req.getParameter("payRate"));
			
			req.getRequestDispatcher("/_view/addTutor.jsp").forward(req, resp);
		}
	}
	
	/**
	 * Validate that the tutor information is submitted correctly.
	 * @param req Request with the submitted information.
	 * @return True if the information if valid, False if it is not valid.
	 */
	private boolean tutorValidate(HttpServletRequest req) {
		
		// Place all parameters in string for readability sake
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String accountNumber = req.getParameter("accountNumber");
		String subject = req.getParameter("subject");
		String payRate = req.getParameter("payRate");
		
		// Assemble inputs into an array to check for empty inputs
		String[] inputs = new String[] {firstName, lastName, username,
										password, email, accountNumber,
										subject, payRate};
		
		// Check all inputs to see if any are empty
		for (String input : inputs) {
			
			if (input.equals("")) {
				
				req.setAttribute("errorMessage", "Please enter all fields");
				return false;
			}
		}
		
		// Check that firstName only contains letters
		if (!firstName.matches("^[a-zA-Z]*$")) {
			
			req.setAttribute("errorMessage",
							 "First Name can only contain letters");
			return false;
		}
		
		// Check that lastName only contains letters
		if (!lastName.matches("^[a-zA-Z]*$")) {
			
			req.setAttribute("errorMessage",
							 "Last Name can only contain letters");
			return false;
		}
		
		// Check that email is in a valid email format
		if (!email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
			
			req.setAttribute("errorMessage",
					 		 "Please enter a valid Email address");
			return false;
		}
		
		// Check that accountNumber is an integer
		try {
			
			Integer.valueOf(accountNumber);
		}
		catch (NumberFormatException e) {
			
			req.setAttribute("errorMessage",
			 		 		 "Account Number can only contain integer numbers");
			return false;
		}
		
		// Check that accountNumber is positive
		if (Integer.valueOf(accountNumber) <= 0) {
			
			req.setAttribute("errorMessage",
	 		 		 		 "Account Number must be positive");
			return false;
		}
		
		// Check that payRate is a double
		try {
			
			Double.valueOf(payRate);
		}
		catch (NumberFormatException e) {
			
			req.setAttribute("errorMessage",
			 		 		 "Pay Rate can only contain decimal numbers");
			return false;
		}
		
		// Check that payRate is positive
		if (Double.valueOf(payRate) <= 0) {
			
			req.setAttribute("errorMessage",
	 		 		 		 "Pay Rate must be positive");
			return false;
		}
		
		return true;
	}
}
