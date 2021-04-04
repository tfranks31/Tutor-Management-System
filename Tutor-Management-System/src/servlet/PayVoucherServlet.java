package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.PayVoucherController;
import model.Entry;

public class PayVoucherServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private int tableSize;
	private PayVoucherController controller = null;
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
			
			controller = new PayVoucherController();
			ArrayList<Entry> entries = controller.getPayVoucherEntries(1);
			
			if (entries.size() < 10) {
				tableSize = 10 - entries.size();
			}
			else {
				tableSize = 0;
			}
			
			req.setAttribute("tableSize", tableSize);
			
			req.setAttribute("entries", entries);
			
			// Call JSP to generate empty form
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("PayVoucher Servlet: doPost");	

		// Go back to search
		if (req.getParameter("addRow") != null) {	
			
			controller = new PayVoucherController();
			ArrayList<Entry> entries = new ArrayList<Entry>();
			
			String[] cells = req.getParameterValues("cell");
			for (int i = 0; i < cells.length; i += 4) {
				
				if (!cells[i].equals("") && !cells[i + 1].equals("") &&
					!cells[i + 2].equals("") && !cells[i + 3].equals("")) {
					
					Entry entry = new Entry();
					entry.setDate(cells[i]);
					entry.setHours(Double.parseDouble(cells[i + 1]));
					entry.setServicePerformed(cells[i + 2]);
					entry.setWherePerformed(cells[i + 3]);
					entries.add(entry);
				}
			}
			
			tableSize = cells.length / 4 - entries.size() + 1;

			if(tableSize + entries.size() > 15) {
				tableSize = cells.length / 4 - entries.size();
			}
			req.setAttribute("tableSize", tableSize);
			req.setAttribute("entries", entries);
			
			req.getRequestDispatcher("/_view/payVoucher.jsp").forward(req, resp);
		}
		
	}
	

}
