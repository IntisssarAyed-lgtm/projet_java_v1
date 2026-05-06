package modele;

public class Utilisateurs {
	private int id;
    private String username;
    private String password;
    private String role; // CLIENT, SERVEUSE, CUISINIER
	public Utilisateurs(int id, String username, String password, String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	public Utilisateurs() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	@Override
	public String toString() {
		return "Utilisateurs [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + "]";
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    
	

}
