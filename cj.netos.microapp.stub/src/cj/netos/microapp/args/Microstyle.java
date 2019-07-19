package cj.netos.microapp.args;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Microstyle {
	String name;
	String title;
	String desc;

	Map<String, Object> assets;
	List<Object> fonts;
	Map<String, Object> colors;
	Map<String, Object> theme;

	public Microstyle() {
		assets = new HashMap<>();
		fonts = new ArrayList<>();
		colors = new HashMap<>();
		theme = new HashMap<>();
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
	public Map<String, Object> getAssets() {
		return assets;
	}

	public Map<String, Object> getColors() {
		return colors;
	}

	public Map<String, Object> getTheme() {
		return theme;
	}

	public void setAssets(Map<String, Object> assets) {
		this.assets = assets;
	}

	public List<Object> getFonts() {
		return fonts;
	}

	public void setFonts(List<Object> fonts) {
		this.fonts = fonts;
	}

	public void setColors(Map<String, Object> colors) {
		this.colors = colors;
	}

	public void setTheme(Map<String, Object> theme) {
		this.theme = theme;
	}

}
