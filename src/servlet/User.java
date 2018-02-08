package servlet;

public class User {
	
	private boolean login = false;
	private String username;
	private String password;
	
	public User() {
		
	}
	
	
	public User(boolean login) {
		
		this.login = login;
	}
	
	public User(boolean login,String username,String password) {
		
		this.login = login;
		this.username = username;
		this.password = password;
	}
	
	public void setLogin(boolean login)
	{
		this.login = login;
		
	}
	
	public boolean getLogin()
	{
		
		return this.login;
	}
	
	
	public String getUsername() {
		
		return this.username;
	}
	
	
	
	
	
	@Override
	public String toString() {
		
		return "Login :"+this.login;
	}

}
