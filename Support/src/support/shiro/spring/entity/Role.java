/**
 * 
 */
package support.shiro.spring.entity;

/**
 * @author chenlf
 * 
 *         2014-3-24
 */
public class Role {
	private String name;

	public Role() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role(String name) {
		super();
		this.name = name;
	}

}
