package servlet;

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.*;
import java.util.*;
import java.net.*;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;


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
        	
        	
        	
        	
        	
        	Context initCtx = new InitialContext();
            if (initCtx == null)
                System.out.println("initCtx is NULL");

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                System.out.println("envCtx is NULL");
            
            
            
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/Fablix");
        	
            if (ds == null)
                System.out.println("ds is null.");
            else 
            	System.out.println("Non problemi");
            
        
            Connection dbcon = ds.getConnection();
        	
            
        	
        	
            //Class.forName("org.gjt.mm.mysql.Driver");
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            // Declare our statement
      
            //Statement statement = dbcon.createStatement();
           
            ///
            // Iterate through each row of rs
           
            String name = request.getParameter("query");
            
            
            int simi = name.length()/4;
            System.out.println(simi);
            simi = Math.max(simi, 1);
            String ps_query = "select title from movies where match(title) against (? in boolean mode) or edth(LCASE(title),LCASE(?),?)=1;";
            //String query = String.format("select title from movies where match(title) against ('+%s*' in boolean mode) or edth(LCASE(title),LCASE('%s'),%d)=1;", name,name,simi);
            //ResultSet rs = statement.executeQuery(query);
            System.out.println(ps_query);
            PreparedStatement statement = dbcon.prepareStatement(ps_query);
            
            
            String name2 = "'" +name+"'";
            
            String[] na =name.split(" ");
            
            String name1 = "";
            for(String s : na)
            {
            	
            	name1+="+"+s+"* ";
            	
            }
            
            name1 = "'"+name1+"'";
            System.out.println(name1);
            statement.setString(1, name1);
            statement.setString(2, name2);
            statement.setInt(3,simi);
            
            
            ResultSet rs = statement.executeQuery();
            
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
            //String query1 = String.format("select name from stars where match(name) against ('+*%s*' in boolean mode) or edth(LCASE(name),LCASE('%s'),%d)=1;", name,name,simi);
            String ps_query2 = "select name from stars where match(name) against (? in boolean mode) or edth(LCASE(name),LCASE(?),?)=1;";
            
            
           
            
            
            PreparedStatement statement2 = dbcon.prepareStatement(ps_query2);
            statement2.setString(1, name1);
            statement2.setString(2, name2);
            statement2.setInt(3,simi);
            ResultSet rs2 = statement2.executeQuery();
            
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
        	System.out.print("lang exception");
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doPOST: " + ex.getMessage() + "</P></BODY></HTML>");
            return;
        }
        out.close();
    }
}
