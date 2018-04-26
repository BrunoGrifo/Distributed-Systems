package hey.action;


import static java.lang.System.out;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hey.model.HeyBean;

public class CreateElectionAction extends Action {
	private static final long serialVersionUID = 4L;
	private String tipo=null, entidade=null, titulo=null, resumo=null,dataI=null, dataF=null;

	@Override
	public String execute() {
		int check=0;
		if(!checkDate(dataI) || !checkDate(dataF))
			check=1;
		if(!checkTipo(tipo))
			check=1;
		
		if(tipo.toUpperCase().equals("FACULTY")) {
			if(!this.getHeyBean().search_faculty(entidade))
				check=1;
		}else if(tipo.toUpperCase().equals("DEPARTMENT")) {
			if(!this.getHeyBean().search_department(entidade))
				check=1;
		}else if(tipo.toUpperCase().equals("NUCLEO")) {
			if(!this.getHeyBean().search_department(entidade))
				check=1;
		}
		
		
		if(check==0) {
			System.out.println("Entrou");
			SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
			try {
				this.getHeyBean().add_eleicao(dt.parse(dataI), dt.parse(dataF), titulo, resumo, tipo.toLowerCase());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			System.out.println("funcionou");
			return SUCCESS;
		}
		else
			System.out.println("The information given doesn't match all the requirements");
			return 	"createE";

	}

	

	public boolean checkDate(String date) {
		System.out.println(date);
		
		SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		try {
			Date write_date_format = dt.parse(date);
			Date date1 = new Date();
			if (date1.after(write_date_format)) {
				System.out.println("Alredy passed that date");
				return false;
			}
		} catch (ParseException e) {
			System.out.println("Error in data format");
			return false;
		}
		return true;

	}
	
	public boolean checkTipo(String role) {
		System.out.println(role);
		if(role.toUpperCase().equals("GERAL") || role.toUpperCase().equals("FACULTY") || role.toUpperCase().equals("DEPARTMENT") || role.toUpperCase().equals("NUCLEO")) {
			return true;
		}else {
			System.out.println("Failed checking type: "+role.toUpperCase());
			return false;	
		}
		

	}

	public String getTipo() {
		return tipo;
	}



	public void setTipo(String tipo) {
		this.tipo = tipo;
	}



	public String getEntidade() {
		return entidade;
	}



	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}



	public String getTitulo() {
		return titulo;
	}



	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}



	public String getResumo() {
		return resumo;
	}



	public void setResumo(String resumo) {
		this.resumo = resumo;
	}



	public String getDataI() {
		return dataI;
	}



	public void setDataI(String dataI) {
		this.dataI = dataI;
	}



	public String getDataF() {
		return dataF;
	}



	public void setDataF(String dataF) {
		this.dataF = dataF;
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
