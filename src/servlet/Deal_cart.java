package servlet;

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Deal_cart extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	

	public String getServletInfo() {
        return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String loginUser = "root";
        String loginPasswd = "0000";
        String loginUrl = "jdbc:mysql://localhost:3306/cs122b";

        response.setContentType("text/html"); // Response mime type
        
        
        String act = request.getParameter("act");
        String id = request.getParameter("name");
        String url = request.getHeader("Referer");
        HttpSession session = request.getSession();
        
        if(act.equals("add"))
        {
        	Map<String,Integer> am = (Map<String, Integer>) session.getAttribute("cart");
        	System.out.println(am.toString());
        	int count = am.get(id);
        	am.put(id, ++count);
        	
        	session.setAttribute("cart", am);
        }
        else if(act.equals("remove"))
        {
        	Map<String,Integer> am = (Map<String, Integer>) session.getAttribute("cart");
        	int count = am.get(id);
        	am.put(id, --count);
        	if(count==0)
        		am.remove(id);
        	
        	session.setAttribute("cart", am);
        	
        }
        
        
        
        response.sendRedirect("./buy");
        System.out.println(url);
        System.out.println(id);
        
        
        
        
        
        // Output stream to STDOUT
        

        
        
    }
}
