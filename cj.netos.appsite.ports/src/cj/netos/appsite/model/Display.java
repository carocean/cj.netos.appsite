package cj.netos.appsite.model;

import java.util.HashMap;
import java.util.Map;

public class Display {
	String id;
	String title;
	String dest;
	Map<String,Object> methods;
	Map<String,Object> properties;
	public Display() {
		methods=new HashMap<>();
		properties=new HashMap<>();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public Map<String, Object> getMethods() {
		return methods;
	}
	public void setMethods(Map<String, Object> methods) {
		this.methods = methods;
	}
	public Map<String, Object> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
}
