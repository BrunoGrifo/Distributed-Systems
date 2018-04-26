package hey.action;
import static java.lang.System.out;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import server.Pessoa;
public class UserPropAction extends Action{

	private static final long serialVersionUID = 1L;
	private String cc = null, password=null, telemovel=null, morada=null, vcc=null, cargo=null, dep=null, fac=null;

	@Override
	public String execute() {
		System.out.println("cc: "+cc);
		System.out.println("pass: "+password);
		System.out.println("tele: "+telemovel);
		System.out.println("morada: "+morada);
		System.out.println("vcc: "+vcc);
		System.out.println("cargo: "+cargo);
		System.out.println("dep: "+dep);
		System.out.println("fac: "+fac);
		
		if(checkCC(cc)) {
			Pessoa person=this.getHeyBean().getPersonbyID(Integer.parseInt(cc));
			if(person==null) {
				System.out.println("Não existe nenhum eleitor com o CC: "+cc);
				return "userProp";
			}
			
			if(password.length()!=0)
				person.setPassword(password);

			if(telemovel.length()!=0) {
				if(checkCP(telemovel))
					person.setTelefone(Integer.parseInt(telemovel));
				else {
					System.out.println("Erro na validação do telemovel");
					return "userProp";
				}
			}
			
			if(morada.length()!=0) 
				person.setMorada(morada);
			
			if(vcc.length()!=0) {
				if(checkDate(vcc)) {
					SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
					try {
						person.setValidade_cc(dt.parse(vcc));
					} catch (ParseException e) {
						System.out.println("Formato Invalido");
						e.printStackTrace();
						return "userProp";
					}
				}else {
					System.out.println("Erro na validação da data");
					return "userProp";
				}
				
			}
			
			if(cargo.length()!=0) {
				person.setCargo(cargo.toLowerCase());
			}
			
			if(dep.length()!=0) {
				if(this.getHeyBean().search_department(dep)==true) {
					if(this.getHeyBean().remove_person_from_old_faculty(person.getNumero_cc()) && this.getHeyBean().remove_person_from_old_department(person.getNumero_cc())) {
						this.getHeyBean().update_props_voter(Integer.parseInt(cc), dep.toUpperCase());
						System.out.println("Trying to Update...");
						if(this.getHeyBean().update_person(person).equals("Person updated\n")) {
							System.out.println("Update successfull");
							return SUCCESS;
						}else {
							System.out.println("Update not successfull");
							return "userProp";
						}
					}
					return SUCCESS;
				}else {
					System.out.println("Erro na validação do departamento");
					return "userProp";
				}
				
			}
			
			if(fac.length()!=0) {
				if(this.getHeyBean().search_faculty(fac)==true) {
					if(this.getHeyBean().remove_person_from_old_faculty(person.getNumero_cc()) && this.getHeyBean().remove_person_from_old_department(person.getNumero_cc())) {
						this.getHeyBean().update_props_voter_fac(Integer.parseInt(cc), fac.toUpperCase());
						System.out.println("Trying to Update...");
						if(this.getHeyBean().update_person(person).equals("Person updated\n")) {
							System.out.println("Update successfull");
							return SUCCESS;
						}else {
							System.out.println("Update not successfull");
							return "userProp";
						}
					}
					return SUCCESS;
				}else {
					System.out.println("Erro na validação da faculdade");
					return "userProp";
				}
			}
			System.out.println("Trying to Update...");
			if(this.getHeyBean().update_person(person).equals("Person updated\n")) {
				System.out.println("Update successfull");
				return SUCCESS;
			}else {
				System.out.println("Update not successfull");
				return "userProp";
			}
		}else {
			System.out.println("Erro na validação do CC");
			return "userProp";
		}
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

	public String getTelemovel() {
		return telemovel;
	}

	public void setTelemovel(String telemovel) {
		this.telemovel = telemovel;
	}

	public String getMorada() {
		return morada;
	}

	public void setMorada(String morada) {
		this.morada = morada;
	}

	public String getVcc() {
		return vcc;
	}

	public void setVcc(String vcc) {
		this.vcc = vcc;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getDep() {
		return dep;
	}

	public void setDep(String dep) {
		this.dep = dep;
	}

	public String getFac() {
		return fac;
	}

	public void setFac(String fac) {
		this.fac = fac;
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
	
	public boolean checkCP(String cp) {
		if (!checkNumbers(cp) || cp.length() != 9) {
			System.out.println("Failed checking CP: "+cp);
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