package servlet;

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;

import java.net.*;



import com.google.gson.JsonObject;


import javax.net.ssl.HttpsURLConnection;
 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class dashboard_insert_movie extends HttpServlet {
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
            
            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            // Declare our statement
      
            CallableStatement statement = dbcon.prepareCall("{call add_movie(?,?,?,?,?,?)}");
            
            
            


        
            
            
            
            
            
            // Iterate through each row of rs
           
            
            //find if star already in the stars table
            
            String mname = request.getParameter("m_name"); // actually min id minus 1
            int myear = Integer.parseInt(request.getParameter("m_year"));
            String mdirector = request.getParameter("m_director");
            String sname = request.getParameter("s_name");
            int syear = Integer.parseInt(request.getParameter("s_year"));
            String gname = request.getParameter("g_name");
            
          
            statement.setString(1, mname);
            statement.setInt(2,myear);
            statement.setString(3, mdirector);
            statement.setString(4,sname);
            statement.setInt(5, syear);
            statement.setString(6,gname);
            
            
            
           
            
            
            JsonObject responseJsonObject = new JsonObject();
            
            ResultSet rs = statement.executeQuery();
    		String result = "";
            while(rs.next())
    			result = rs.getString(1);
            responseJsonObject.addProperty("status",result);
            	
            
           
            
            response.getWriter().write(responseJsonObject.toString());
         
            
            
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
