/**
 * 
 */
package support.shiro.spring.entity;


/**
 * @author chenlf
 * 
 *         2014-3-24
 */
public class User {
	private String name;
	private String password;
	private Role role;

	public User() {

	}
	
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

}
