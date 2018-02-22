package test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.HashSet;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



//basic logic flow read xml parse to csv , write load query load csv to database//

public class DomParser {
	
	
	HashMap<String,movie> movieMap;
	HashMap<String,star>  starMap;
	HashMap<String,ArrayList<String>> castMap; 
	Document dom;
	Document actor;
	Document cast;
	
	public DomParser()
	{
		movieMap = new HashMap<String,movie>(); // movie id -> movie
		starMap = new HashMap<String,star>(); // star name -> star 
		castMap = new HashMap<String,ArrayList<String>>(); //movie id -> star name list
	}
	
	public void run() {
        //parse the xml file and get the dom object
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();
        parseActors();
        parseCasts();
        System.out.println(movieMap.get("CC82").title);
        
        //insert movie at first 
        
        System.out.println(starMap.get("Willie Aames"));
        //then insert star and link them 
        
        System.out.println(castMap.size());
		//insert genre finally
        
        insert();
        
      
       
        
	}
	
	private void insert() {
		
        String loginUser = "root";
        String loginPasswd = "0000";
        String loginUrl = "jdbc:mysql://localhost:3306/cs122b";
		
        
        try {
        	
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        	Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        	Statement s1 = dbcon.createStatement();
        	//perform a dupication check before each batch insert
        	ResultSet rs1  = s1.executeQuery("select distinct(title),id from movies;");
        	
        	HashMap<String,String> movie_titles = new HashMap<String,String>(); //title->id
        	HashMap<String,String> star_names = new HashMap<String,String>(); //title->id
        	HashMap<String,Integer> genre_names = new HashMap<String,Integer>(); //title->id
        	
        	while(rs1.next())
        	{
        		movie_titles.put(rs1.getString(1),rs1.getString(2));
        	}
        	
        	rs1.close();
        	s1.close();
        	
        	Statement s2  = dbcon.createStatement();
        	ResultSet rs2  = s2.executeQuery("select distinct(name),id from stars;");
        	
        	
        	while(rs2.next())
        	{
        		star_names.put(rs2.getString(1),rs2.getString(2));
        	}
        	
        	rs2.close();
        	s2.close();
        	
        	 s2  = dbcon.createStatement();
        	 rs2  = s2.executeQuery("select distinct(name),id from genres;");
        	
        	
        	while(rs2.next())
        	{
        		genre_names.put(rs2.getString(1),rs2.getInt(2));
        	}
        	
        	rs2.close();
        	s2.close();
        	
        	int g_max = 1998;
        	s2 = dbcon.createStatement();
        	rs2 = s2.executeQuery("select max(id)+1 from genres;");
        	while(rs2.next())
        		g_max = rs2.getInt(1);
        	
        	rs2.close();
        	s2.close();
        	
        	int s_min = 1105;
        	s2 = dbcon.createStatement();
        	rs2 = s2.executeQuery("select min(id)-1 from stars;");
        	while(rs2.next())
        		s_min = Integer.parseInt(rs2.getString(1));
        	
        	rs2.close();
        	s2.close();
        	
        	
        	System.out.println("star size from xml file "+starMap.size());
        	
        	System.out.println("movies size " + movie_titles.size());
        	System.out.println("genre size " + genre_names.size());
        	System.out.println("star size " + star_names.size());
        	
        	
        	
        	
        	
        	
        	PreparedStatement ps1=null;
        	PreparedStatement ps2=null;
        	PreparedStatement ps3=null;
        	PreparedStatement ps4=null;
        	PreparedStatement ps5=null;
        	
        	int[] iNoRows=null;
        	int[] iNoRows1=null;
        	int[] iNoRows2=null;
        	int[] iNoRows3=null;
        	int[] iNoRows4=null;
        	
        	String insert_movie="insert into movies values(?,?,?,?)";
        	String insert_star = "insert into stars values(?,?,?)";
        	String insert_genre = "insert into genres values(?,?)";
        	String link_star = "insert into stars_in_movies values(?,?)";
        	String link_genre = "insert into genres_in_movies values(?,?)";
        	
        
        	
        	try {
        		dbcon.setAutoCommit(false);
        		ps1=dbcon.prepareStatement(insert_movie);
        		ps2=dbcon.prepareStatement(insert_star);
        		ps3=dbcon.prepareStatement(insert_genre);
        		ps4=dbcon.prepareStatement(link_star);
        		ps5=dbcon.prepareStatement(link_genre);
        		
        		
        		int count = 0;
        		Iterator it = movieMap.entrySet().iterator();
        	    while (it.hasNext()) {
        	            Map.Entry pair = (Map.Entry)it.next();
        	            
        	            movie m = ((movie)pair.getValue());
        	            
        	            if(!movie_titles.containsKey(m.title)&&m.title!=null && !m.title.equals("NULL")) {
        	            	String m_id = movieIdEncode(m.id);
        	            	ps1.setString(1, m_id);
        	            	
        	            	String m_title = "NULL";
        	            	if(m.title!=null)
        	            		m_title = m.title;
        	            	ps1.setString(2, m_title);
        	            	
        	            	int m_year = 0000;
        	            	
        	            	ps1.setInt(3, m.year);
        	            	
        	            	String m_director = "NULL";
        	            	if(m.director!=null)
        	            		m_director = m.director;
        	            	ps1.setString(4, m_director);
        	            	ps1.addBatch();
        	            	movie_titles.put(m_title,m_id);
        	            	for(int i=0;i<m.genre.size();i++) {
        	            		
        	            		String g_name = m.genre.get(i);
        	            		if(!genre_names.containsKey(g_name)&&g_name!=null) {
        	            			int g_id = count+g_max;
        	            			ps3.setInt(1, g_id);
        	            			ps3.setString(2, g_name);
        	            			ps3.addBatch();
        	            			count++;
        	            			ps5.setInt(1, g_id);
        	            			ps5.setString(2,m_id);
        	            			ps5.addBatch();
        	            			genre_names.put(g_name,g_id);  //Prohibiting duplicate genre insertion
        	            		}
        	            		else { //linkiing
        	            			if(g_name!=null) {
        	            			ps5.setInt(1, genre_names.get(g_name));
        	            			ps5.setString(2, m_id);
        	            		    ps5.addBatch();
        	            			}
        	            		}
        	            		
        	            		
        	            	}
        	            
        	            
        	            }
        	            
        	            
        	            //it.remove(); // avoids a ConcurrentModificationException
        	    }
        		
        	    it = castMap.entrySet().iterator();
        	    int count_s = 0;
        	    while (it.hasNext()) {
        	    	Map.Entry pair = (Map.Entry)it.next();
        	    	ArrayList<String> sList = (ArrayList<String>) pair.getValue();
        	    	
        	    	
        	    	
        	    	String c_id = (String) pair.getKey();//raw movie id
        	    	String c_title = "NULL";
        	    	try {
        	    		c_title = movieMap.get(c_id).title;
        	    	}
        	    	catch(NullPointerException e)
        	    	{
        	    		System.out.println("invalid movie id:"+c_id);
        	    		
        	    	}
        	    	if(movie_titles.containsKey(c_title)) // we can only insert with a exist movie
    	    			c_id = movie_titles.get(c_title);
        	    	else
        	    		c_id = "NULL";
    	    		
        	    	
        	    	for(int i=0;i<sList.size();i++)
        	    	{
        	    		String s_id ="";
        	    		
        	    		
        	    		
        	    		
        	    		
        	    		
        	    		
        	    		String s_name = sList.get(i);
        	    		if(!star_names.containsKey(s_name)&&!s_name.equals("NULL")&&!c_id.equals("NULL")) {
        	    			
        	    			
        	    			int s_year = 0000;
        	    			try {
        	    			 s_year = ((star)starMap.get(s_name)).year;
        	    			 ps2.setInt(3, s_year);
        	    			}
        	    			catch(NullPointerException e){
        	    				
        	    				ps2.setNull(3, java.sql.Types.INTEGER);
        	    			}
        	    			s_id = movieIdEncode(String.format("%d", (s_min - count_s)));
        	    			ps2.setString(1, s_id);
        	    			ps2.setString(2, s_name);
        	    			
        	    			ps2.addBatch();
        	    			ps4.setString(1, s_id);
        	    			ps4.setString(2,c_id);
        	    			ps4.addBatch();
        	    			star_names.put(s_name,s_id);
        	    			count_s++;
        	    		}
        	    		else {
        	    			s_id = star_names.get(s_name);
        	    			if(s_id!=null && c_id!="NULL") {
        	    				ps4.setString(1, s_id);
        	    				ps4.setString(2, c_id);
        	    				ps4.addBatch();
        	    			}
        	    		}
        	    		
        	    		
        	    		
        	    		
        	    		
        	    	}
        	    	
        	    	
        	    }
        	    
        	    
        	    iNoRows = ps1.executeBatch();
        	    iNoRows1=ps3.executeBatch();
        	    iNoRows2=ps5.executeBatch();
        	    iNoRows3=ps2.executeBatch();
        	    iNoRows4=ps4.executeBatch();
        	    
        	    
        	    
        	    dbcon.commit();
        		
        	}
        	catch (SQLException e)
        	{
        		e.printStackTrace();
        	}
        	
        	
        	
        }
        
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	
        }
		
		
	}
	
	
	private String movieIdEncode(String id) {
		
		return "tt89" + id; //empirically unique id
	}
	
	private void parseXmlFile() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            //parse using builder to get DOM representation of the XML file
            dom = db.parse("mains243.xml");
            actor = db.parse("actors63.xml");
            cast = db.parse("casts124.xml");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
		
	}
	
	
	private void parseDocument() {
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("film");
		if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Employee object
                movie e = getMovie(el);

                //add it to list
                movieMap.put(getTextValue(el,"fid"),e);
            }
        }
		
	}
	
	private void parseActors() {
		Element docEle = actor.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("actor");
		if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Employee object
                star e = getStar(el);

                //add it to list
                
                
                starMap.put(e.name,e);
            }
        }
		
	}
	
	
	private void parseCasts() {
		Element docEle = cast.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("filmc");
		
		
		if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Employee object
                

                //add it to list
                NodeList nl1 = el.getElementsByTagName("m");
                for(int j=0;j<nl1.getLength();j++)
                {
                	Element el1  = (Element) nl1.item(j);
                	
                	String key = getTextValue(el1,"f");
                	String value = getTextValue(el1,"a");
                	
                	if(castMap.containsKey(key))
                	{
                		ArrayList<String> a = castMap.get(key);
                		if(!value.equals("s a"))
                		  a.add(value);
                		castMap.put(key, a);
                	}
                	else {
                		ArrayList<String> a = new ArrayList();
                		if(!value.equals("s a")) //"s a" is one of toxic wildcard in casts file
                			a.add(value);
                		castMap.put(key, a);
                	}
                	
                }
                
                
            }
        }
		
	}
	
	
	
	
	private star getStar(Element empEl)
	{
		String name = getTextValue(empEl,"stagename");
		int year = getIntValue(empEl,"dob");
		
		star s = new star(name,year);
		
		return s;
	}
	
	
	
	
    private movie getMovie(Element empEl) {

        //for each <employee> element get text or int values of 
        //name ,id, age and name
        String id = getTextValue(empEl, "fid");
        String title = getTextValue(empEl, "t");
        int year = getIntValue(empEl, "year");
        String director = "";
        ArrayList<String> genre  = new ArrayList<String>();
        
        NodeList nl = empEl.getElementsByTagName("dirs");
        if(nl!=null&&nl.getLength()>0)
        {
        	Element el = (Element) nl.item(0);
        	NodeList nl1 = el.getElementsByTagName("dir");
        	if(nl1!=null&&nl.getLength()>0)
        	{
        		Element el1 = (Element)nl1.item(0);
        		director = getTextValue(el1,"dirn");
        	}
        	
        }
        
        NodeList nl2  = empEl.getElementsByTagName("cats");
        if(nl2!=null&&nl2.getLength()>0)
        {
        	for(int i=0;i<nl2.getLength();i++)
        	{
        		genre.add ( getTextValue((Element)nl2.item(0),"cat") );
        	}
        	
        }
        

        String type = empEl.getAttribute("type");

        //Create a new Employee with the value read from the xml nodes
        movie e = new movie(id, title, year, director,genre);

        return e;
    }
    
    private String getTextValue(Element ele, String tagName) {
        
    	try {
    	String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    	}
    	catch(NullPointerException e) {
    		return "NULL";
    	}
    }

    /**
     * Calls getTextValue and returns a int value
     * 
     * @param ele
     * @param tagName
     * @return
     */
    private int getIntValue(Element ele, String tagName) {
        //in production application you would catch the exception
    	try {
         return Integer.parseInt(getTextValue(ele, tagName));
         }
    	catch(NumberFormatException e){
    		return 0000; 
    	}
    	
    }
	
	
	
    public static void main(String[] args) 
	{
		DomParser p = new DomParser();
		
		p.run();
	}
 
}
