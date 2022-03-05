package pl.octicos.zakupy;

import java.io.Serializable;

public class Produkt implements Serializable{
	private static final long serialVersionUID = 2L;
	private String nazwa;
	private String ilosc;
	private String nazwaIlosc;
	private boolean kupiony;
	
	
	
	public Produkt(String nazwa, String ilosc, boolean kupiony) {
		this.nazwa = nazwa;
		this.ilosc = ilosc;
		this.kupiony = kupiony;
		this.nazwaIlosc = this.nazwa + " - " + this.ilosc + " - " + (this.kupiony ? 1 : 0) ;
	}
	
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	public boolean isKupiony() {
		return kupiony;
	}
	public void setKupiony(boolean kupiony) {
		this.kupiony = kupiony;
		this.nazwaIlosc = this.nazwa + " - " + this.ilosc + " - " + (kupiony ? 1 : 0) ;
	}
	public String getIlosc() {
		return ilosc;
	}
	public void setIlosc(String ilosc) {
		this.ilosc = ilosc;
	}
	public String getNazwaIlosc() {
		return nazwaIlosc;
	}
	public void setNazwaIlosc(String nazwaIlosc) {
		this.nazwaIlosc = nazwaIlosc;
	}
	
	
	
	

}
