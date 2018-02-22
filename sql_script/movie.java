package test;
import java.util.ArrayList;

public class movie {
	
	public String id;
	public String title;
	public int year;
	public String director;
	public ArrayList<String> genre;
	
	public movie() {
		
		
	}
	
	public movie(String id,String title,int year,String director,ArrayList<String> genre)
	{
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.genre = genre;
	}
	
	
	public String getId() {
		
		return this.id;
	}
	
	
	public String getTitle() {
		
		return this.title;
	}
	
	public int getYear() {
		
		
		return this.year;
	}
	
	public String getDirector()
	{
		
		return this.director;
	}
	
	public void setId(String id)
	{
		
		this.id = id;
	}
	
	public void setTitle(String title)
	{
		
		this.title = title;
	}
	
	
	public void setYear(int year)
	{
		
		this.year = year;
	}
	
	public void setDirector(String director)
	{
		
		this.director = director;
	}
	
	
	
	
	public String toQuery() {
		
		return String.format("insert into movies value('%s',%s,'%d',%s) ", this.id,this.title,this.year,this.director);
	}
	
	public String toString() {
		//return a query of generate by this object
		
		if(genre.size()>=1)
			return String.format("%s %s %d %s %s", this.id,this.title,this.year,this.director,this.genre.get(0));
		return String.format("%s %s %d %s", this.id,this.title,this.year,this.director);
	}
	
	
}
