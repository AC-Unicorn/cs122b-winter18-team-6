<%@page language = "java" %>
<%@page import = "java.io.*" %>
<%@page import = "javax.servlet.http.*"%>
<%@page import = "java.util.HashMap"%>
<%@page import = "java.util.Map.Entry" %>>



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
            String s = "";
            if(session1.getAttribute("cart")==null)
            {
            	s = "Empty cart";
            	s += "<a href ='./Main'>Back to Home Page</a>";
            }//First time access website create login attribute
            else {
            	
            	HashMap<String,Integer> am = (HashMap<String, Integer>) session1.getAttribute("cart");
            	
            	for(Entry<String,Integer> e : am.entrySet() )
            	{
            		if( e.getKey()!=null){
            	 		String key  = e.getKey();
            			s+= "Movie : "+ key + " Quantity : " + e.getValue() +" <button id = '"+ key +"' onclick = foo(this.id)>add</button> <button id='"+ key +"' onclick = foo2(this.id)>remove</button> <br> ";
            		}
            	}
            	
            	s += "<a href ='./check_out'>Continue check out</a>";
            }
			
			System.out.println("Hello world!"); 
	%>
    <h1>Mizushima's Open Art Gallery</h1>
    <div id="login_div">
    
        <p><%=s%></p>
   
    </div>          


	<script type="text/javascript">
		 function foo(id)
		 {
			 
			 var url = "./Deal_cart?act=add&name="+id; 
      		 window.location.href = url; 
			 
		 }
		 function foo2(id)
		 {
			 
			 var url = "./Deal_cart?act=remove&name="+id; 
     		 window.location.href = url; 
			 
		 }	
	</script>

  </body>
  <foot>
      <p>All rights reserved by 42</p>
  </foot>

























</html>