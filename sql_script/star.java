package test;

public class star {

	public String name;
	public int year;
	public star() {}
	public star(String name,int year) {
		
		this.name = name;
		this.year = year;
	}
	
	
	
	
	
	
	
	
	
	public String toQuery() {
		
		String id = "";
		
		
		return String.format("insert into stars value(%s,%s,%d)",id,this.name,this.year );
	}
	
	
	public String toString() {
		
		return String.format("%s , %d",this.name,this.year );
		
	}
	
}
