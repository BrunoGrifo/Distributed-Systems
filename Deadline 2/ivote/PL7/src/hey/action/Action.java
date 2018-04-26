package hey.action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import hey.model.HeyBean;;

public class Action extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 3L;
	protected Map<String, Object> session;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public HeyBean getHeyBean() {
		if (!session.containsKey("heyBean")) {
				try {
					this.setHeyBean(new HeyBean());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
		}
		return (HeyBean) session.get("heyBean");
	}

	public void setHeyBean(HeyBean heyBean) {
		this.session.put("heyBean", heyBean);
	}
}

