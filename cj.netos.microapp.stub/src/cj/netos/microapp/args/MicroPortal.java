package cj.netos.microapp.args;

import java.util.HashMap;
import java.util.Map;

public class MicroPortal {
	String creator;
	String name;
	String version;
	String title;
	String desc;
	String ctime;
	String useStyle;
	String developer;
	Map<String, String> plugin;
	Map<String,Microdisplay> displays;
	Map<String,Microstyle> styles;
	public MicroPortal() {
		this.plugin=new HashMap<>();
		this.displays=new HashMap<>();
		this.styles=new HashMap<>();
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public String getUseStyle() {
		return useStyle;
	}
	public void setUseStyle(String useStyle) {
		this.useStyle = useStyle;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public Map<String, String> getPlugin() {
		return plugin;
	}
	public void setPlugin(Map<String, String> plugin) {
		this.plugin = plugin;
	}
	public Map<String, Microdisplay> getDisplays() {
		return displays;
	}
	public void setDisplays(Map<String, Microdisplay> displays) {
		this.displays = displays;
	}
	public Map<String, Microstyle> getStyles() {
		return styles;
	}
	public void setStyles(Map<String, Microstyle> styles) {
		this.styles = styles;
	}
	
	
}
