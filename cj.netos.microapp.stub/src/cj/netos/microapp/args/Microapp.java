package cj.netos.microapp.args;

import java.util.Map;

public class Microapp {
	String name;
	String title;
	String desc;
	String developer;
	String applyVersion;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getApplyVersion() {
		return applyVersion;
	}
	public void setApplyVersion(String applyVersion) {
		this.applyVersion = applyVersion;
	}
	
	
}
