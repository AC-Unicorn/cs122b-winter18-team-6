



<!doctype html>
<html>
  <head>
  
    <meta charset="UTF-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <style>
       
    
      body{
        background:url("http://game-insider.com/wp-content/uploads/2011/02/Plain-Blak-Gray.jpg");
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
        display:block;	
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
        
      #dashboard_div{
      		display:none;
      		color:white;
      }  
        
      #add_movie input{
         
      	 
      	 display:inline;
      }
        
    </style>
  <title>Fablix dashboard entry</title>
  </head>




  <body>
  
   
    
    <div id="login_div">
    <form id="login" method="post" >
        <p>Username<br></p>
 	<input type="text" name="Username">
        <p><br>Password<br></p>        
        <input type="password" name = "Password" >
        <br>
        <input type="submit" value="Login">
        
        
    </form>
    </div>          
    
    
    <div id="dashboard_div">
      <form id="add_star" method="post" >
        
        <p>Star name</p>        
        <input type="text" name = "name" >
        <p>Birth year</p>
        <input type="text" name="birth_year">
        <input type="submit" value="add a star">
        <p id="status"></p>
        
      </form>
      <h2>Our current databases</h2>
      <div id="show_tables"></div>
      
      <form id="add_movie" method="post" >
        
        <p>Movie name</p>        
        <input type="text" name = "m_name" >
        <p>Publish year</p>
        <input type="number" name="m_year">
        <p>Director</p>
        <input type="text" name = "m_director" value="none">
        <p>Star name</p>
        <input type="text" name = "s_name" value="none">
        <p>Star birth year</p>
        <input type="number" name="s_year">
        <p>Genre</p>
        <input type="text" name="g_name">
        <input type="submit" value="add a movie">
        <p id="m_status"></p>
        
      </form>
      
    </div>      
    


	<script>
	
		function handleLoginResult(resultDataString) {
			resultDataJson = JSON.parse(resultDataString);
		
			

			// if login success, redirect to index.html page
			if (resultDataJson["status"] == "0") {
				
				
				$('#login_div').fadeOut();
				$('#dashboard_div').fadeIn();
			    
			    var s = "<ul>";
			    
			    for(var key in resultDataJson){	
			      if(key!="status")	
			        s += "<li>" + key +" : " +resultDataJson[key]+"</li>";
			    }
			    
			    s+= "</ul>";
			    
			    $('#show_tables').html(s);
			    
			    
			} else {
				$('#login_div').html("<p>login fail</p><a href='./_dashboard'>Login Again</a>");
				
			}
		}
	
	
	
	    
		function submitLoginForm(formSubmitEvent) {
			console.log("submit login form");
		
		// important: disable the default action of submitting the form
		//   which will cause the page to refresh
		//   see jQuery reference for details: https://api.jquery.com/submit/
			formSubmitEvent.preventDefault();
			
			jQuery.post(
				"./dashboard_verify", 
				// serialize the login form to the data sent by POST request
				jQuery("#login").serialize(),
				(resultDataString) => handleLoginResult(resultDataString));

		}
		
		jQuery("#login").submit((event) => submitLoginForm(event));
		
		
		
		
		//seperation line
		
		
		function handleInsertResult(resultDataString)
		{
			resultDataJson = JSON.parse(resultDataString);
			
			var s = resultDataJson["status"];
			$('#status').html(s);
			
		}
		
		
		
		function insertStar(e)
		{
			
			e.preventDefault();
			jQuery.post(
					"./dashboard_insert_star", 
					// serialize the login form to the data sent by POST request
					jQuery("#add_star").serialize(),
					(resultDataString) => handleInsertResult(resultDataString));
			
			
		}
		
		
		
		jQuery("#add_star").submit((event) => insertStar(event));
		
		
		//seperation line for add movie
		
		function handleInsertResult2(resultDataString)
		{
			resultDataJson = JSON.parse(resultDataString);
			
			var s = resultDataJson["status"];
			$('#m_status').html(s);
			
		}
		
		
		
		function insertMovie(e)
		{
			
			e.preventDefault();
			jQuery.post(
					"./dashboard_insert_movie", 
					// serialize the login form to the data sent by POST request
					jQuery("#add_movie").serialize(),
					(resultDataString) => handleInsertResult2(resultDataString));
			
			
		}
		
		
		
		jQuery("#add_movie").submit((event) => insertMovie(event));
		
		
	</script>

  </body>
  <foot>
      <p>All rights reserved by 42</p>
  </foot>

























</html>