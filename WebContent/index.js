function test(id)
{
	  	    	
	  	    	var url = "./browse?name="+id;
	  	    	window.location.href = url;
}

//test is used to handle browsing options

	  	    
function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	console.log("sending AJAX request to backend Java Servlet")
	
	// TODO: if you want to check past query results first, you can do it here
	
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
	jQuery.ajax({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "movie-suggestion?query=" + escape(query),
		"success": function(data) {
			// pass the data, query, and doneCallback function into the success handler
			handleLookupAjaxSuccess(data, query, doneCallback) 
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	})
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	
	// parse the string into JSON
	var jsonData = JSON.parse(data);
	var limited_js_data = [];
	var m_count = 0;
	var s_count = 0;
	for(var i = 0;i<jsonData.length;i++) //limit 
	{
		
		console.log(jsonData[i]['data']);
		if(m_count<10&&jsonData[i]["category"]=="movie")
		{	
			limited_js_data.push(jsonData[i]);
			m_count++;
		}
		else if(s_count<10&&jsonData[i]["category"]=="star")
		{
			limited_js_data.push(jsonData[i])
			s_count++;
		}
	}
	
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: limited_js_data } );
}


/*
 * This function is the select suggestion hanlder function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"])
	var url = suggestion["data"] + "-hero" + "?search=" + suggestion["data"]
	console.log(url)
	handleNormalSearch(suggestion["value"],suggestion["value"],suggestion["category"]);
}


/*
 * This statement binds the autocomplete library with the input box element and 
 *   sets necessary parameters of the library.
 * 
 * The library documentation can be find here: 
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 * 
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set the groupby name in the response json data field
    
    // set delay time
    deferRequestBy: 300,
    groupBy: "category",
    minChars:3
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as mininum characters
});


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(id,query,cat) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
	var url = "";
	if(cat=="movie")
		url = "./Search?Search=" + escape(query);
	else if(cat=="star")
		url = "./SingleStar?name=" +escape(query);
	window.location.href = url;
	
}

// bind pressing enter key to a hanlder function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the hanlder function
		handleNormalSearch($('#autocomplete').val())
	}
}) 