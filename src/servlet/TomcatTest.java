package servlet;

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TomcatTest extends HttpServlet {
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

        out.println("<HTML><HEAD><TITLE>Movie List</TITLE></HEAD>");
        out.println("<style> body{background:url('https://static.independent.co.uk/s3fs-public/thumbnails/image/2015/06/03/13/inception.jpg'); padding:0;margin:0;} #nav{height:20px;width100%;margin:0;background-color:yellow; } #banner{ height:120px;width:100%;background:url('http://www.publicdomainpictures.net/pictures/80000/velka/logo-banner.jpg');margin:0; }  ");
        out.println("ul{display:block;background-color:white;border:1px solid;border-radius:5%;opacity:0.5;}");
       
        
        
        
        out.println("</style>");
        out.println("<BODY><div id='nav'> <a>Home</a> <a>Movie</a> <a>Purchase</a>    </div>");
        out.println("<div id='banner'><h1>Fablix Top 20</h1></div>");
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            // Declare our statement
      
            Statement statement = dbcon.createStatement();
            
            String query = "Select * from movies,ratings  where movies.id = ratings.movieId order by rating desc limit 20;";
            
            // Perform the query
            ResultSet rs = statement.executeQuery(query);
               
            
          
            

            // Iterate through each row of rs
            while (rs.next()) {
                
                
                String m_title = rs.getString(2);
                String m_year = rs.getString(3);
                String m_director = rs.getString(4);
                String m_rating = rs.getString(6);
               
                
                String query2 = "select * from movies,genres_in_movies,genres where movies.id='"+rs.getString(1) +"' and movies.id = movieId and genreId = genres.id;";
                
                Statement statement2 = dbcon.createStatement();
                ResultSet rs2 = statement2.executeQuery(query2);
                String m_genres = "";
                while(rs2.next())m_genres += rs2.getString(8)+",";
                
                
                String query3 = "select * from movies,stars_in_movies,stars where movies.id='"+rs.getString(1) +"' and movies.id = movieId and starId = stars.id;";
                
                String m_stars = "";
                Statement statement3 = dbcon.createStatement();
                ResultSet rs3 = statement3.executeQuery(query3);
                while(rs3.next()) m_stars += rs3.getString(8) + ",";
                
                
                out.println("<div class='movie_box'>");
                out.println( "<ul> <li>Title: " + m_title + "</li>" + "<li>Year: " + m_year + "</li>" + "<li>Director: " + m_director + "</li>" + "<li>Rate: "
                        +m_rating+"</li>"+"<li>genre: "+m_genres+"</li>"+"<li>Actors: " +m_stars+ "</li>" +"</ul>");
            }

            
             
            out.println("<p> To be or not to be?</p>");
         
            
            
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
