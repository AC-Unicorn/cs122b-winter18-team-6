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
import javax.servlet.http.HttpSession;

public class browse_title_reuslt extends HttpServlet {
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
        
        String input = request.getParameter("Search");
        
        
        int offset = 0;
        int pg = 1;
        if(request.getParameter("page_num")!=null) {
          pg = Integer.parseInt(request.getParameter("page_num"));	
          offset = Integer.parseInt(request.getParameter("page_num"))-1;
          
        }
        else
          pg = 1;
        
        offset*=20;
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
        		"}#page_control{margin-top:90%;margin-left:400px;display:block;position:absolute;width:800px;}");
        
        
        out.println("</style>");
        out.println("<BODY><div id='nav'> <ul><li>Sorted By:</li><li><a href='./btr?Search="+input+"&page_num="+pg+"&sort=tacd'>title acsending</a></li>"+
        		""+"<li><a href='./btr?Search="+input+"&page_num="+pg+"&sort=tdcd'>title descding</a></li>"
        		+"<li><a href='./btr?Search="+input+"&page_num="+pg+"&sort=yacd'>Year acsending</a></li>"
        		+"<li><a href='./btr?Search="+input+"&page_num="+pg+"&sort=ydcd'>Year descding</a></li>"
        		+ "<li>Home</li> <li>Movie</li> <li><a href='./buy'>Purchase</a></li></ul>    </div>");
        out.println("<div id='banner'><img id='top20'src='https://www.pcc.edu/about/awards/images/top-20.png' alt='Top 20' width = 150px; >  </div>");
        out.println("<div class=wrapper>");
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            // Declare our statement
      
            Statement statement = dbcon.createStatement();
            
           
            String colunmn = "title";
            String order_type = "asc";
            if(request.getParameter("sort")!=null) {
            	 String sort_order = request.getParameter("sort");
            	 if(sort_order.equals("tdcd")) {
            		 order_type = "desc";
            	 }
            	 else if(sort_order.equals("yacd"))
            	 {
            		 colunmn = "year";
            	
            	
            	 }
            	 else if(sort_order.equals("ydcd")){
            	
            		 order_type = "desc";
            		 colunmn = "year";
            	 }
            }
            
            
            System.out.println(input);
            String query = "Select distinct(movies.id),title,year,director from movies  where  movies.title like '"+input+"%'  "
                   + " order by "+colunmn+" "+ order_type+"  limit 20 offset "+offset+" ;";
            
            
            
            

            // Perform the query
            ResultSet rs = statement.executeQuery(query);
               
            //Count rows
            int  movie_nums = 0;
            String count_query = "Select count(distinct(movies.id)) from movies  where  movies.title like '"+input+"%'  ";
            Statement count_state = dbcon.createStatement();
            ResultSet count = count_state.executeQuery(count_query);
            while(count.next()) {
            	movie_nums = count.getInt(1);
            }
            movie_nums/=20;
            count.close();
            count_state.close();
            
           
            
            //Count rows ends
          
            

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
                while(rs3.next()) m_stars += "<a id ='"+rs3.getString(8)+"' onclick = foo(this.id)>"+rs3.getString(8) + "</a>,";
                
                rs3.close();
                statement3.close();
                
                
                
                
                out.println("<div class='movie_box'>");
                out.println( "<ul> <li><span class='title_text'><a id='"+m_title+"' onclick='test(this.id)'> " + m_title + "</a></span></li>" + "<li>Year: " + m_year + "</li>" + "<li>Director: " + m_director + "</li>"
                        +"<li>genre: "+m_genres+"</li>"+"<li>Actors: " +m_stars+ "</li>" +"<a href='./checkout?name="+m_title+"'><img src='https://d30y9cdsu7xlg0.cloudfront.net/png/28468-200.png' width='40px' heigth='40px'></a>"+"</ul>");
                out.println("</div>");
                
                
                out.println("<script>\r\n" + 
                		"	   function test(id){\r\n" + 
                		"		   \r\n" + 
                		"		   \r\n" + 
                		"		   var url = \"./SingleMovie?name=\"+id;\r\n" + 
                		"		   \r\n" + 
                		"		   window.location.href = url;\r\n" + 
                		"		  \r\n" + 
                		"	   }	\r\n" + 
                		"	\r\n" + 
                		"	\r\n" + 
                		"	function foo(id){"
                		+ ""
                		+ ""
                		+ "var url = './SingleStar?name='+id; window.location.href = url;}</script>");
                
                
            }

            
            
            
            
             
            out.println("</div>");
            
            
            out.println("<div id='page_control'>");
            out.println("<ul >");
            out.println("<listyle='display:inline;'><a href=./btr?Search="+input+"&page_num="+Math.max((pg-1),1)+"><img src='https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Angle_left_font_awesome.svg/768px-Angle_left_font_awesome.svg.png' width='20px' height='20px'></a></li>");
            if(movie_nums>20)
            {
            	for(int i = (0+pg);i<Math.min((21+pg),movie_nums);i++)
            	{
            		out.println("<li style='display:inline;'><a style = 'text-decoration:none;color:white;background:gray;'href=./btr?Search="+input+"&page_num="+i+">"+i+"</a></li>");
            	}
            }
            
            else 
            {
            	for(int i =1;i<movie_nums;i++)
            	{
            		out.println("<li style='display:inline;'><a style ='text-decoration:none;color:white;background:gray; ' href=./btr?Search="+input+"&page_num="+i+">"+i+"</a></li>");
            	}
            	
            	
            }
            
            out.println("<li style='display:inline;'><a href=./btr?Search="+input+"&page_num="+Math.min((pg+1), movie_nums)+"><img src='https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/Angle_right_font_awesome.svg/120px-Angle_right_font_awesome.svg.png' width='20px' height='20px'></a></li>");
            out.println("</ul>");
            out.println("</div>");
            
            
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