package servlet;

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AndroidSearch extends HttpServlet {
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
        
        String input = request.getParameter("title");
        
        //this will be write in url 
        
        
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            // Declare our statement
      
            Statement statement = dbcon.createStatement();
            
           
            String colunmn = "title";
            String order_type = "asc";
            
            
            
            
            //
            
            
            
            String new_input = "match(title) against ('+";
            

            
            new_input += input;
            
            new_input += "*' in boolean mode) or edth(title,'"+input+"',2)=1";
            System.out.println(new_input);
            
            String query = "Select distinct(movies.id),title,year,director from movies  where   "
            		+ new_input+" ;";
            
            //
            
            
            
            //this will be write in url 
            

            // Perform the query
            ResultSet rs = statement.executeQuery(query);
               
           
            
            
            
            
            JsonArray ja = new JsonArray();//returning json
          
            
            

            // Iterate through each row of rs
            while (rs.next()) {
                
                
                String m_title = rs.getString(2);
                String m_year = rs.getString(3);
                String m_director = rs.getString(4);
                
               
                
                String query2 = "select * from movies,genres_in_movies,genres where movies.id='"+rs.getString(1) +"' and movies.id = movieId and genreId = genres.id;";
                
                Statement statement2 = dbcon.createStatement();
                ResultSet rs2 = statement2.executeQuery(query2);
                String m_genres = "";
                while(rs2.next())m_genres += rs2.getString(8)+",";
                
                rs2.close();
                statement2.close();
                
                
                String query3 = "select * from movies,stars_in_movies,stars where movies.id='"+rs.getString(1) +"' and movies.id = movieId and starId = stars.id;";
                
                String m_stars = "";
                Statement statement3 = dbcon.createStatement();
                ResultSet rs3 = statement3.executeQuery(query3);
                while(rs3.next()) m_stars += rs3.getString(8) + ",";
                
                rs3.close();
                statement3.close();
                
                
                JsonObject obj = new JsonObject();
                obj.addProperty("title", m_title);
                obj.addProperty("year", m_year);
                obj.addProperty("dir", m_director);
                obj.addProperty("genres", m_genres);
                obj.addProperty("stars", m_stars);
                
                ja.add(obj);
                
            }

            
            
            
            
             
            response.getWriter().write(ja.toString());
            
            
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
                    + "<P>SQL error in doGet: " + ex.getMessage() + "</P></BODY></HTML>");
            return;
        }
        out.close();
    }
}
