package servlet;

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class browse extends HttpServlet {
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
        
        
        
        
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Movie List</TITLE></HEAD>");
        out.println("<style>");
       
        
        out.println("body{font-family:times,serif;background:white; padding:0;margin:0px;} #nav{ height:60px;width100%;margin-bottom:0;text-align:center; } #banner{ height:140px;background-color:white;margin-top:0;padding:0;item-align:center; }  \r\n" + 
        		".movie_box ul{background-color:white;margin:0; height:100%;width:100%} li{list-style-type:none; }\r\n" + 
        		"\r\n" + 
        		"\r\n" + 
        		"#top20{\r\n" + 
        		"margin-left:46.8%;\r\n" + 
        		"}\r\n" + 
        		"\r\n" + 
        		"#nav ul{\r\n" + 
        		"display:inline-block;\r\n" + 
        		"padding:0;\r\n" + 
        		"margin:0;\r\n" + 
        		"margin-left:20px;\r\n" + 
        		"}\r\n" + 
        		"\r\n" + 
        		"#nav li{\r\n" + 
        		"margin-top:15px;\r\n" + 
        		"margin-left:55px;\r\n" + 
        		"color:white;\r\n" + 
        		"display:block;\r\n" + 
        		"float:left;\r\n" + 
        		"color:white;\r\n" + 
        		"font-size:1.4em;\r\n" + 
        		"}\r\n" + 
        		"\r\n" + 
        		"\r\n" + 
        		"#twenty{color:yellow; }\r\n" + 
        		"\r\n" + 
        		".movie_box{\r\n" + 
        		"float:left;\r\n" + 
        		"width:250px;\r\n" + 
        		"height:250px;\r\n" + 
        		"background:url('https://i.pinimg.com/originals/9f/87/64/9f8764ef849360e7eaee2365ca8a810b.jpg');\r\n" + 
        		"margin:1% 2%;\r\n" + 
        		"text-align:left;\r\n" + 
        		"\r\n" + 
        		"color:black;\r\n" + 
        		"opacity:0.7;\r\n" + 
        		"}\r\n" + 
        		"\r\n" + 
        		".title_text{\r\n" + 
        		"   font-size:1.5em;\r\n" + 
        		"   color:RGB(0,128,153);\r\n" + 
        		"   text-decoration:blink;\r\n" + 
        		"}\r\n" + 
        		"\r\n" + 
        		".wrapper{\r\n" + 
        		"	width:80%;\r\n" + 
        		"	margin: 0 10%;\r\n" + 
        		"}");
        
        
        out.println("</style>");
        out.println("<BODY><div id='nav'> <ul><li><a href='./Main'>Home</a></li> <li>Movie</li> <li>Purchase</li></ul>    </div>");
        out.println("<div id='banner'><img id='top20'src='https://www.pcc.edu/about/awards/images/top-20.png' alt='Top 20' width = 150px; >  </div>");
        out.println("<div class=wrapper>");
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            

            
            
            String query = "select * from genres;";
            
            if(id.equals("right"))
            {
            	out.println("<div class='movie_box'>");
            	for(int i=0;i<10;i++)
            	{
            		out.println( "<ul> <li><span class='title_text'><a href='./Search?Search="+i+"'>Title start with: " + i + "</a></span></li>" );
            	}
            	out.println("</ul>");
            	char c = 'a';
            	for(int i=0;i<26;i++)
            	{
            		char ci = (char) (c+i);
            		out.println( "<ul> <li><span class='title_text'><a href='./Search?Search="+ci+"'>Title start with: " + ci + "</a></span></li>" );
            	}
            	out.println("</ul>");
            	 out.println("</div>");
            }
            else {
                Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
                
                // Declare our statement
          
                Statement statement = dbcon.createStatement();
            	ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    
                    
                    String m_title = rs.getString(2);                    
                    out.println("<div class='movie_box'>");
                    out.println( "<ul> <li><span class='title_text'><a href='./browse_genre?Search="+m_title+"'>Title: " + m_title + "</a></span></li>" );
                    out.println("</ul>");
                    out.println("</div>");
                    
                }
                rs.close();
                
                statement.close();
                
                dbcon.close();
            }
            
           


            
             
           
         
            
            

            
            
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
