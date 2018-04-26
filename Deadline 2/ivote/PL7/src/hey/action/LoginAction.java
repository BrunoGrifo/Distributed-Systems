/**
 * Raul Barbosa 2014-11-07
 */
package hey.action;

import java.rmi.RemoteException;
import java.util.Map;
import hey.model.HeyBean;

public class LoginAction extends Action{
	private static final long serialVersionUID = 4L;
	private String username = null, password = null;

	@Override
	public String execute() {	
		// any username is accepted without confirmation (should check using RMI)
		System.out.println(username);
		try {
			if (username.toUpperCase().equals("ADMIN") && password.toUpperCase().equals("ADMIN")) {
				this.getHeyBean().setUsername(this.username);
				this.getHeyBean().setPassword(this.password);
				session.put("username", username);
				session.put("loggedin", true); // this marks the user as logged in
				return "regist";
			}
			if(this.username != null && !username.equals("") && this.getHeyBean().getUserMatchesPassword(username,password)) {
				this.getHeyBean().setUsername(this.username);
				this.getHeyBean().setPassword(this.password);
				session.put("username", username);
				session.put("loggedin", true); // this marks the user as logged in
				return SUCCESS;
			}
			else
				return LOGIN;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return LOGIN;
	}
	
	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
	}

	public void setPassword(String password) {
		this.password = password; // what about this input? 
	}
	
	/*public HeyBean getHeyBean() {
		if(!session.containsKey("heyBean"))
			this.setHeyBean(new HeyBean());
		return (HeyBean) session.get("heyBean");
	}

	public void setHeyBean(HeyBean heyBean) {
		this.session.put("heyBean", heyBean);
	}*/

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
