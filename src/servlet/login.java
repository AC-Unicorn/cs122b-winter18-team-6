package servlet;

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.net.*;



import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.net.ssl.HttpsURLConnection;
 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class login extends HttpServlet {
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
            DataSource ds = (DataSource) envCtx.lookup("jdbc/Fablix");
            Connection dbcon =ds.getConnection();
            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            // Declare our statement
      
            Statement statement = dbcon.createStatement();
            
            HttpSession session = request.getSession();
            
            if(session.getAttribute("login")==null)
            {
            	
            	
            	session.setAttribute("login", 0);
            	
            }//First time access website create login attribute
//            else if(  (int)session.getAttribute("login")==1) {
//            	
//            	
//            	
//            	response.sendRedirect("./Main");
//            }
//            
            /// recaptcha
        	String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        	System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
        	boolean valid = VerifyUtils.verify(gRecaptchaResponse);
           
            
            ///
            
               
            
            out.println("<h1>Log in success</h1>");
            

            // Iterate through each row of rs
           
            String uname = request.getParameter("Username");
            String upasscode = request.getParameter("Password");
            
            String query = "select password,id from customers  where customers.email = '"+uname+"';";
            ResultSet rs = statement.executeQuery(query);
            boolean flag = false;
            int ccid = 0;
            while(rs.next()) {
            	
            	if(upasscode.equals(rs.getString(1))){
            		flag = true;
            		ccid = rs.getInt(2);
            	}
            	
            	
            }
            
            session.setAttribute("user_id",ccid );
            
            if(flag==false || !valid )
             response.sendRedirect("./loginerror");
            else 
            {
           
             session.setAttribute("login",1 );
             session.setAttribute("user_id",ccid );
             response.sendRedirect("./Main");
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
