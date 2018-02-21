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



import com.google.gson.JsonObject;
import javax.net.ssl.HttpsURLConnection;
 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class dashboard_verify extends HttpServlet {
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
      
            Statement statement = dbcon.createStatement();
            
            
            


        
            
            ///
            
               
            
           
            

            // Iterate through each row of rs
           
            String uname = request.getParameter("Username");
            String upasscode = request.getParameter("Password");
            
            String query = "select password from employees  where employees.email = '"+uname+"';";
            ResultSet rs = statement.executeQuery(query);
            boolean flag = false;
            int ccid = 0;
            while(rs.next()) {
            	
            	if(upasscode.equals(rs.getString(1))){
            		flag = true;
            		
            	}
            	
            	
            }
            
            JsonObject responseJsonObject = new JsonObject();
            if(flag)
            {
            	
    			responseJsonObject.addProperty("status", "0");
    			
    			Statement show_tables = dbcon.createStatement();
    			ResultSet rs1 = show_tables.executeQuery("show tables");
    			
    			while(rs1.next())
    			{
    				
    				Statement show_columns = dbcon.createStatement();
    				
    				String sc = String.format("show columns from %s", rs1.getString(1));
    				
    				ResultSet rs2 = show_columns.executeQuery(sc);
    				
    				String details = " this table has following attributes and types ";
    				
    				while(rs2.next())
    				{
    					
    					details += " column: " +rs2.getString(1)+ " types is: "+rs2.getString(2)+"";
    				}
    				
    				rs2.close();
    				show_columns.close();
    				
    				responseJsonObject.addProperty(rs1.getString(1), details);
    			}
    			
    			
    			
    			rs1.close();
    			show_tables.close();
            }
            else {
            	
    			responseJsonObject.addProperty("status", "1");
            	
            }
            
            response.getWriter().write(responseJsonObject.toString());
         
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
