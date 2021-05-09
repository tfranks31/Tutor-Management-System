package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AddTutorController;
import model.Pair;
import model.Tutor;
import model.UserAccount;

public class AddTutorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AddTutorController controller; 
	private boolean editTutor;
	private boolean tutorProfile;
	private boolean addTutor;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("AddTutor Servlet: doGet");	
		
		UserAccount account = (UserAccount) req.getSession().getAttribute("user");
		
		if (account != null) {
			editTutor = (boolean)req.getSession().getAttribute("edit");
			req.setAttribute("edit", editTutor);
			tutorProfile = (boolean)req.getSession().getAttribute("viewProfile");
			req.setAttribute("viewProle", tutorProfile);
			addTutor = (boolean)req.getSession().getAttribute("addTutor");
			req.setAttribute("addTutor", addTutor);
		}
		
		// If user not logged in, redirect to login
		if (account == null) {
			
			System.out.println("AddTutor Servlet: null account");
			
			resp.sendRedirect("login");
		}

		// Go back to search
		else if (req.getParameter("back") != null) {
			
			System.out.println("AddTutor Servlet: back");
			
			resp.sendRedirect("search");
		} else if (editTutor) {
			
			System.out.println("AddTutor Servlet: edit load");
			
			controller = new AddTutorController();
			
			UserAccount user = (UserAccount) req.getSession().getAttribute("editUser");
			Tutor tutor = (Tutor) req.getSession().getAttribute("editTutor");
			String[] name = tutor.getName().split(" ");
			
			req.setAttribute("firstName", name[0]);
			req.setAttribute("lastName", name[1]);
			req.setAttribute("username", user.getUsername());
			req.setAttribute("password", user.getPassword());
			req.setAttribute("email", tutor.getEmail());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("accountNumber", tutor.getAccountNumber());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("subject", tutor.getSubject());
			
			req.getRequestDispatcher("/_view/addTutor.jsp").forward(req, resp);
		} else if(tutorProfile) {
			System.out.println("AddTutor Servlet: profile load");
			
			controller = new AddTutorController();
			
			//UserAccount user = (UserAccount) req.getSession().getAttribute("editUser");
			Tutor tutor = (Tutor) req.getSession().getAttribute("tutorProfileInfo");
			String[] name = tutor.getName().split(" ");
			
			req.setAttribute("firstName", name[0]);
			req.setAttribute("lastName", name[1]);
			req.setAttribute("username", account.getUsername());
			req.setAttribute("password", account.getPassword());
			req.setAttribute("email", tutor.getEmail());
			req.setAttribute("studentID", tutor.getStudentID());
			req.setAttribute("accountNumber", tutor.getAccountNumber());
			req.setAttribute("payRate", tutor.getPayRate());
			req.setAttribute("subject", tutor.getSubject());
			
			req.getRequestDispatcher("/_view/addTutor.jsp").forward(req, resp);
		}
		
		// Load addTutor
		else {
			
			System.out.println("AddTutor Servlet: default Load");
			
			// Call JSP to generate empty form
			req.getRequestDispatcher("/_view/addTutor.jsp").forward(req, resp);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("AddTutor Servlet: doPost");
		
		controller = new AddTutorController();
		
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String username = email.split("@")[0];
		String studentID = req.getParameter("studentID");
		String accountNumber = req.getParameter("accountNumber");
		String subject = req.getParameter("subject");
		String payRate = req.getParameter("payRate");
		
		// If the tutor information is valid, continue to the search page
		if (tutorValidate(req)) {
			if (req.getParameter("addTutor") != null) {
				
				System.out.println("AddTutor Servlet: tutorAdded");
				
				UserAccount newAccount = new UserAccount();
				newAccount.setUsername(username);
				newAccount.setPassword(password);
				newAccount.setIsAdmin(false);
				
				Tutor newTutor = new Tutor();
				newTutor.setName(firstName + " " + lastName);
				newTutor.setEmail(email);
				newTutor.setStudentID(studentID);
				newTutor.setAccountNumber(accountNumber);
				newTutor.setSubject(subject);
				newTutor.setPayRate(Double.parseDouble(payRate));
				
				controller.addTutor(newAccount, newTutor);
				
				req.setAttribute("tutorName", firstName + " " + lastName);
				
				resp.sendRedirect("search");
				
			}else if (req.getParameter("editTutorInfo") != null) {
				
				System.out.println("AddTutor Servlet: tutorEdited");
									
				UserAccount updatedAccount = new UserAccount();
				updatedAccount.setUsername(username);
				updatedAccount.setPassword(password);
				updatedAccount.setIsAdmin(false);
				updatedAccount.setAccountID(((UserAccount) req.getSession().getAttribute("editUser")).getAccountID());
				
				Tutor updatedTutor = new Tutor();
				updatedTutor.setName(firstName + " " + lastName);
				updatedTutor.setEmail(email);
				updatedTutor.setStudentID(studentID);
				updatedTutor.setAccountNumber(accountNumber);
				updatedTutor.setSubject(subject);
				updatedTutor.setPayRate(Double.parseDouble(payRate));
				updatedTutor.setTutorID(((Tutor) req.getSession().getAttribute("editTutor")).getTutorID());
				
				controller.editTutor(updatedAccount, updatedTutor);
				req.setAttribute("editTutorName", firstName + " " + lastName);
				resp.sendRedirect("search");
				
			}else if (req.getParameter("updatePassword") != null) {
				
				System.out.println("AddTutor Servelet: Update Password");
			
				UserAccount account = (UserAccount) req.getSession().getAttribute("user");
				
				controller.updatePassword(account, password);
				
				req.setAttribute("editPasswordName", firstName + " " + lastName);
				resp.sendRedirect("search");
				
			}
			
		}
		// If the tutor information is invalid, set all parameters with inputed
		// parameters and reload the add tutor page with proper error message
		else {
			
			req.setAttribute("firstName", firstName);
			req.setAttribute("lastName", lastName);
			req.setAttribute("username", username);
			req.setAttribute("password", password);
			req.setAttribute("email", email);
			req.setAttribute("studentID", studentID);
			req.setAttribute("accountNumber", accountNumber);
			req.setAttribute("subject", subject);
			req.setAttribute("payRate", payRate);
			
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
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String username = email.split("@")[0];
		String studentID = req.getParameter("studentID");
		String accountNumber = req.getParameter("accountNumber");
		String subject = req.getParameter("subject");
		String payRate = req.getParameter("payRate");
		
		// Assemble inputs into an array to check for empty inputs
		String[] inputs = new String[] {firstName, lastName, username,
										password, email, studentID,
										accountNumber, subject, payRate};
		
		// Check all inputs to see if any are empty
		for (String input : inputs) {
			
			if (input.equals("")) {
				
				req.setAttribute("errorMessage", "Please enter all fields");
				return false;
			}
		}
		
		// Check that firstName only contains letters
		if (!firstName.matches("^[a-zA-Z]*$")) {
			
			req.setAttribute("errorMessage", "First Name can only contain letters");
			return false;
		}
		
		// Check that lastName only contains letters
		if (!lastName.matches("^[a-zA-Z]*$")) {
			
			req.setAttribute("errorMessage", "Last Name can only contain letters");
			return false;
		}
		
		// Check that email is in a valid email format
		if (!email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
			
			req.setAttribute("errorMessage", "Please enter a valid Email address");
			return false;
		}
		
		// Check that student ID only contains numbers
		if (!studentID.matches("^[0-9]*$")) {
			
			req.setAttribute("errorMessage", "Student ID can only contain numbers");
			return false;
		}
		
		// Check that payRate is a double
		try {
			
			Double.valueOf(payRate);
		}
		catch (NumberFormatException e) {
			
			req.setAttribute("errorMessage", "Pay Rate can only contain decimal numbers");
			return false;
		}
		
		// Check that payRate is positive
		if (Double.valueOf(payRate) <= 0) {
			
			req.setAttribute("errorMessage", "Pay Rate must be positive");
			return false;
		}
		
		return true;
	}
}