package hey.action;


import static java.lang.System.out;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hey.model.HeyBean;

public class RegisterAction extends Action {
	private static final long serialVersionUID = 4L;
	private String ccv = null, name = null, cc = null, password = null, phone = null, adress = null, role = null;

	@Override
	public String execute() {
		if (checkDate(ccv) && checkCC(cc) && checkCP(phone) && checkRole(role)) {
			if(this.getHeyBean().registerVoter(name,cc,ccv,password,phone,adress,role.toLowerCase()))
				return SUCCESS;
			else
			
				return "regist";
		}
		return "regist";
		

	}

	public String getCcv() {
		return ccv;
	}

	public void setCcv(String ccv) {
		this.ccv = ccv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean checkDate(String date) {
		System.out.println(date);
		
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date write_date_format = dt.parse(date);
		} catch (ParseException e) {
			System.out.println("Error in data format");
			return false;
		}
		return true;

	}

	public boolean checkCC(String cc) {
		if (!checkNumbers(cc) || cc.length() != 8) {
			System.out.println("Failed checking CC");
			return false;
		}
		return true;

	}

	public boolean checkCP(String cp) {
		if (!checkNumbers(cp) || cp.length() != 9) {
			System.out.println("Failed checking CP: "+cp);
			return false;
		}
		return true;
	}

	public boolean checkRole(String role) {
		System.out.println(role);
		if(role.toUpperCase().equals("STAFF") || role.toUpperCase().equals("STUDENT") || role.toUpperCase().equals("TEACHER")) {
			return true;
		}else {
			System.out.println("Failed checking Role: "+role.toUpperCase());
			return false;	
		}
		

	}

	public boolean checkNumbers(String num) {
		int i = 0;
		char[] array = num.toCharArray();
		while (num.length() != i) {
			if ((int) array[i] > 57 || (int) array[i] < 48) {
				out.println("Can not start or end with a space and can only ccontain numbers!");
				return false;
			}
			i++;
		}
		if (num.length() == 0) {
			out.println("Can not start or end with a space and can only ccontain numbers!");
			return false;
		}
		return true;
	}

}
