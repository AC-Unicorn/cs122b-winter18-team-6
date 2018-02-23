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

import java.util.UUID;

import com.google.gson.JsonObject;
import javax.net.ssl.HttpsURLConnection;
 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class dashboard_insert_star extends HttpServlet {
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
            System.out.println("BreakPoint0");
            //find max stars id   
            
            
            //end 
            
            
            
            // Iterate through each row of rs
           
            
            //find if star already in the stars table
            UUID uuid  =  UUID.randomUUID(); 
            String id = uuid.toString().substring(0, 8); // actually min id minus 1
            String sname = request.getParameter("name");
            String byear = request.getParameter("birth_year");
            
            
            System.out.println("BreakPoint1");
            
            
          //find if exist such star
            int count_star = 0;
            Statement repeat  = dbcon.createStatement();
            ResultSet repeatRs = repeat.executeQuery("select count(id) from stars where name ='"+sname+"';");
            while(repeatRs.next())
             count_star = repeatRs.getInt(1);
            repeatRs.close();
            repeat.close();
            
            //

            System.out.println("BreakPoint2");
            
            
            
            String query = String.format("insert into stars values('%s','%s','%s')", id,sname,byear);
            
            
            JsonObject responseJsonObject = new JsonObject();
            if(count_star==0)
            {
            	int rs = statement.executeUpdate(query);
    			System.out.println(rs);
            	responseJsonObject.addProperty("status", "successfully insert");
            	
            }
            else {
            	System.out.println(query);
    			responseJsonObject.addProperty("status", "insert fail dupilcate record");
            	
            }
            
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
