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
		
		// Call JSP to generate empty form
		req.getRequestDispatcher("/_view/addTutor.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("AddTutor Servlet: doPost");
		
		String errorMessage = null;
		
		
		
		req.setAttribute("errorMessage", errorMessage);
		
		req.getRequestDispatcher("/_view/addTutor.jsp").forward(req, resp);
	}
}
