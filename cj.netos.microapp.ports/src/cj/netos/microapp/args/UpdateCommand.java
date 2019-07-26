package cj.netos.microapp.args;

public class UpdateCommand {
	String microappname;
	String microappversion;
	String command;//DA微应用不存在;D删除的版本；N新增的版本；微应用一旦发布不可修改，故而无修改命令
	public String getMicroappname() {
		return microappname;
	}
	public void setMicroappname(String microappname) {
		this.microappname = microappname;
	}
	public String getMicroappversion() {
		return microappversion;
	}
	public void setMicroappversion(String microappversion) {
		this.microappversion = microappversion;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
}
