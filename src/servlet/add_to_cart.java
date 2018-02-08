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

public class add_to_cart extends HttpServlet {
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
        
        
        
        String id = request.getParameter("name");
        String url = request.getHeader("Referer");
        HttpSession session = request.getSession();
        
        if(session.getAttribute("cart")==null)
        {
        	
        	Map<String,Integer> am = new HashMap<String,Integer>();
        	am.put(id, 1);
            session.setAttribute("cart", am);
            
        }
        else {
        	
        	Map<String,Integer> am = (Map<String, Integer>) session.getAttribute("cart");
        	
        	if(am.containsKey(id))
        	{
        		int count = am.get(id);
        		am.put(id, ++count);
        		
        		System.out.println(am.get(id));
        	}
        	else {
        		
        		am.put(id,1);
        	}
        	
        	session.setAttribute("cart", am);
        	
        }
        
        
        
        response.sendRedirect(url);
        System.out.println(url);
        System.out.println(id);
        
        
        
        
        
        // Output stream to STDOUT
        

        
        
    }
}
