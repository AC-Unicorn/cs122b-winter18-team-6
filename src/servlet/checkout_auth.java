package servlet;

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Date;

public class checkout_auth extends HttpServlet {
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String loginUser = "root";
        String loginPasswd = "0000";
        String loginUrl = "jdbc:mysql://localhost:3306/cs122b";
        
        response.setContentType("text/html"); // Response mime type
        
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        
        
        
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
        	Context initCtx = new InitialContext();
            if (initCtx == null)
                System.out.println("initCtx is NULL");
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                System.out.println("envCtx is NULL");
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/Fablix_write");
            Connection dbcon =ds.getConnection();
            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            // Declare our statement
      
            Statement statement = dbcon.createStatement();
            HttpSession session = request.getSession();
            
            
            
            
               
            
            
            

            // Iterate through each row of rs
           
            String lastName = request.getParameter("lastName");
            String firstName = request.getParameter("firstName");
            String cardNumber = request.getParameter("creditCard");
            String eDate = request.getParameter("expireDate");
            
            
            String query = "select * from creditcards where id = '"+cardNumber+"';";
            ResultSet rs = statement.executeQuery(query);
            boolean flag = false;
            while(rs.next()) {
            	
            	if(firstName.equals(rs.getString(2)) && lastName.equals(rs.getString(3)) && eDate.equals(rs.getString(4)))
            		flag = true;
            	
            }
            
            
            if(flag==true)
            {	
            	if(session.getAttribute("cart")==null)
            		response.sendRedirect("./auth_error");
            	else {
            		
            		HashMap<String,Integer> am = (HashMap<String, Integer>) session.getAttribute("cart");
            		System.out.println(am.toString());            		
            		am.remove(null);
            		for (String str : am.keySet()) {  
            		      System.out.println("key is :"+ str);
            		      
            		      if(!str.equalsIgnoreCase("null")) {
            		      String m_id = "select id from movies where title='"+str+"';";
            		      Statement s1 = dbcon.createStatement();
            		      ResultSet rs1 = s1.executeQuery(m_id);
            		      String id = "";
            		      while(rs1.next()) id = rs1.getString(1);
            		      rs1.close();
            		      s1.close();
            		      
            		      Date dt = new Date();
            		      System.out.println(dt.getYear()+1900);
            		      System.out.println(dt.getMonth()+1);
            		      System.out.println(dt.getDate());
            		      
            		      String dts = dt.getYear()+1900 +"/" + (dt.getMonth() +1) +"/" +dt.getDate();
            		      
            		      int ccId = (int) session.getAttribute("user_id");
            		      System.out.println(id);
            		      if(id!=null&&id!="") {
            		      String update = "INSERT INTO sales VALUES(0,"+ccId+",'"+id+"', '"+dts+"');";
            		      Statement s2 = dbcon.createStatement();
            		      int n = s2.executeUpdate(update);
            		      
            		      System.out.println(n+"rows has been changed");
            		      s2.close();
            		      }
            		      }
            		}  
            		
            		am = new HashMap<String,Integer>(); // reset cart
            		session.setAttribute("cart", am);
            		
            	}
            	
            	response.sendRedirect("./auth_success");
            	
            	
            }
            else {
            	
            	response.sendRedirect("./auth_error");
            }
            
            
           
            rs.close();
            
            statement.close();
            
            dbcon.close();
        } catch (SQLException ex) {
            while (ex != null) {
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            } // end while
        } // end catch SQLException

        catch (java.lang.Exception ex) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doPOST: " + ex.getMessage() + "</P></BODY></HTML>");
            return;
        }
        out.close();
    }
}
