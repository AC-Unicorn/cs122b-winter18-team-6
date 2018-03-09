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
import com.google.gson.JsonArray;
import javax.net.ssl.HttpsURLConnection;
 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class suggestion extends HttpServlet {
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
           
            String name = request.getParameter("query");
            
            
            String query = String.format("select title from movies where match(title) against ('+%s*' in boolean mode) or edth(title,'%s',3)=1;", name,name);
            ResultSet rs = statement.executeQuery(query);
           
            JsonArray rsArray = new JsonArray();
            
            while(rs.next()) {
            	
            	
            	JsonObject mv = new JsonObject();
            	JsonObject nest_obj = new JsonObject();
            	mv.addProperty("category", "movie"); 	
            	mv.addProperty("value", rs.getString(1));
            	nest_obj.addProperty("category", "movie");
            	mv.add("data", nest_obj);
            	rsArray.add(mv);
            	
            }
            
            
            
            
            //search in stars
            String query1 = String.format("select name from stars where match(name) against ('+*%s*' in boolean mode) or edth(name,'%s',2)=1;", name,name);
            
            
            Statement statement2 = dbcon.createStatement();
            ResultSet rs2 = statement2.executeQuery(query1);
            
            while(rs2.next())
            {
            	JsonObject st = new JsonObject();
            	JsonObject nest_obj = new JsonObject();
            	st.addProperty("category", "star"); 	
            	st.addProperty("value", rs2.getString(1));
            	
            	nest_obj.addProperty("category", "star");
            	st.add("data", nest_obj);
            	
        		
            	
            	
            	rsArray.add(st);
            	
            }
            
            rs2.close();
            statement2.close();
            
          
            response.getWriter().write(rsArray.toString());
         
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
