package pl.octicos.zakupy;

import java.io.Serializable;

public class Uzytkownik implements Serializable {
	private static final long serialVersionUID = 3L;
	private String email;
	private String pass;
	private boolean polaczony;
	private boolean czyTworzyListe;
	public Uzytkownik(String email, String pass, boolean polaczony) {
		this.email = email;
		this.pass = pass;
		this.polaczony = polaczony;
		this.czyTworzyListe = true;
	}
	
	
	public boolean isCzyTworzyListe() {
		return czyTworzyListe;
	}


	public void setCzyTworzyListe(boolean czyTworzyListe) {
		this.czyTworzyListe = czyTworzyListe;
	}


	public Uzytkownik(String email, boolean polaczony) {
		super();
		this.email = email;
		this.polaczony = polaczony;
	}


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public boolean isPolaczony() {
		return polaczony;
	}
	public void setPolaczony(boolean polaczony) {
		this.polaczony = polaczony;
	}
	
	public boolean polacz(String email) {
		
		
		return true;
	}
	

}
