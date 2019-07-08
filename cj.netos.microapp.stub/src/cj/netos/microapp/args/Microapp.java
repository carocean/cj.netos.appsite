package cj.netos.microapp.args;

import java.util.Map;

public class Microapp {
	String from;
	String theme;
	String home;
	Microsite microsite;
	Map<String,Micropage> pages;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public Microsite getMicrosite() {
		return microsite;
	}
	public void setMicrosite(Microsite microsite) {
		this.microsite = microsite;
	}
	public Map<String, Micropage> getPages() {
		return pages;
	}
	public void setPages(Map<String, Micropage> pages) {
		this.pages = pages;
	}
	
	
}
