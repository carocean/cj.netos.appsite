package cj.netos.appsite.model;

import java.util.HashMap;
import java.util.Map;

public class Appsite {
	String portal;
	String home;
	String error;
	Map<String,Page> pages;

    public Appsite() {
        pages=new HashMap<>();
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, Page> getPages() {
        return pages;
    }

    public void setPages(Map<String, Page> pages) {
        this.pages = pages;
    }
}
