package cj.netos.appsite.model;

import java.util.HashMap;
import java.util.Map;

public class Portal {
	String id;
	String title;
	String desc;
	String ctime;
	String defaultStyle;
	String developer;
	Map<String, Display> displays;
	Map<String, Style> styles;
	public Portal() {
		this.displays=new HashMap<>();
		this.styles=new HashMap<>();
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getDefaultStyle() {
		return defaultStyle;
	}

	public void setDefaultStyle(String defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public Map<String, Display> getDisplays() {
		return displays;
	}

	public void setDisplays(Map<String, Display> displays) {
		this.displays = displays;
	}

	public Map<String, Style> getStyles() {
		return styles;
	}

	public void setStyles(Map<String, Style> styles) {
		this.styles = styles;
	}
}
