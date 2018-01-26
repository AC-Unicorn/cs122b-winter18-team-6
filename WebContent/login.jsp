<%@page language = "java" %>
<%@page import = "java.io.*" %>
<%@page import = "javax.servlet.http.*"%>



<!doctype html>
<html>
  <head>
    <style>
       
    
      body{
        background:url("https://upload.wikimedia.org/wikipedia/commons/1/1b/Claude_Monet_-_Woman_with_a_Parasol_-_Madame_Monet_and_Her_Son_-_Google_Art_Project.jpg");
        background-size:cover;
      }
        
      h1{
           font-family: Brush Script MT, Brush Script Std, cursive;
           color: white;
        }    
      #login_div{
        width:300px;
        height:400px;
        margin: 25% 40%;
	padding:0;
        opacity:0.7;
        background-color:#f3f3f3;	
      }
      #login{
	width:73%;	
        height:80%;
        padding:0px;
        padding-top:60px;
        margin:3% 13.5%;
        
        
      }    
      #login input{
        float:center;
        width:190px;
	height:30px;
        margin:0;
      }
      #login p{
        margin:0;
	font-size:0.9em;
        font-family:'Helvetica Neue',Helvetica,Arial,sans-serif;
      } 

      foot p{
         font-family:Brush Script MT, Brush Script Std, cursive;;
         float:right;
         color: ivory;
         font-size: 1.4em;  
      }
        
        
    </style>
  <title>Fablix</title>
  </head>




  <body>
  
    <%  
		    HttpSession session1 = request.getSession();
            
            if(session1.getAttribute("login")==null)
            {
            	session1.setAttribute("login", 0);
            }//First time access website create login attribute
            else if((int) session1.getAttribute("login")==1) {
            	
            	response.sendRedirect("./Main");
            }
			session1.setAttribute("login",0);
			System.out.println("Hello world!"); 
	%>
    <h1>Mizushima's Open Art Gallery</h1>
    <div id="login_div">
    <form id="login" method="POST" action="./form">
        <p>Username<br></p>
 	<input type="text" name="Username">
        <p><br>Password<br></p>        
        <input type="password" name = "Password" >
        <br>
        <input type="submit" >
        
        <a href="https://www.google.com/search?ei=9DNmWvb-JoesjwPbwYTgBQ&q=dumbest+people+&oq=dumbest+people+&gs_l=psy-ab.3..0l2.15603.28417.0.28585.41.25.11.4.4.0.101.1771.23j1.24.0....0...1c.1.64.psy-ab..3.38.1863...46j0i67k1j0i131i67k1j0i131k1j0i10k1j0i46k1.0.ssu4k2s3cQ8">Forget password?</a>
        <p>It is necessary to login before you view any web content</p>
    </form>
    </div>          




  </body>
  <foot>
      <p>All rights reserved by 42</p>
  </foot>

























</html>