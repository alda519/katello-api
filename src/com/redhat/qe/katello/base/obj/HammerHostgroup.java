package com.redhat.qe.katello.base.obj;

import java.util.logging.Logger;

import javax.management.Attribute;

import com.redhat.qe.katello.base.threading.KatelloCliWorker;
import com.redhat.qe.tools.SSHCommandResult;

public class HammerHostgroup extends _HammerObject{
    protected static Logger log = Logger.getLogger(HammerHostgroup.class.getName());

	// ** ** ** ** ** ** ** Public constants
	public static final String CLI_CMD_CREATE = "hostgroup create";
	public static final String CLI_CMD_INFO = "hostgroup info";
	public static final String CMD_LIST = "hostgroup list";
	public static final String CMD_DELETE = "hostgroup delete";
	public static final String CMD_UPDATE = "hostgroup update";
	
	public static final String OUT_CREATE = 
			"Hostgroup created";
	public static final String OUT_UPDATE = 
			"Hostgroup [ %s ] updated.";
	public static final String OUT_DELETE = 
			"Hostgroup [ %s ] deleted.";
	
	public static final String ERR_NAME_EXISTS = 
			"Name has already been taken";
	public static final String ERR_NOT_FOUND =
			"Hostgroup with id '%s' not found";
	
	// ** ** ** ** ** ** ** Class members
	public String Id;
	public String name;
	
	public HammerHostgroup(){super();}
	
	public HammerHostgroup(KatelloCliWorker kcr, String pName){
		this.name = pName;
		this.kcr = kcr;
	}
	
	public String getName() {
	    return name;
	}
	
	public void setName(String name) {
	    this.name = name;
	}

	public SSHCommandResult cli_create(){		
		args.clear();
		args.add(new Attribute("name", this.name));
		return run(CLI_CMD_CREATE);
	}
	
	public SSHCommandResult cli_info(){
		args.clear();
		args.add(new Attribute("name", this.name));
		return run(CLI_CMD_INFO);
	}
	
	public SSHCommandResult cli_list(){
		args.clear();
		return run(CMD_LIST);
	}

	public SSHCommandResult cli_search(String search){
		args.clear();
		args.add(new Attribute("search", search));
		return run(CMD_LIST);
	}
	
	public SSHCommandResult cli_list(String order, Integer page, Integer per_page){
		args.clear();
		args.add(new Attribute("order", order));
		args.add(new Attribute("page", page));
		args.add(new Attribute("per-page", per_page));
		return run(CMD_LIST);
	}
	
	public SSHCommandResult cli_list(String searchStr, String order, String page, Integer per_page) {
		args.clear();
		args.add(new Attribute("search", searchStr));
		args.add(new Attribute("order", order));
		args.add(new Attribute("page", page));
		args.add(new Attribute("per-page", per_page));
		return run(CMD_LIST);
	}
	
	public SSHCommandResult update(String new_name){
		args.clear();
		args.add(new Attribute("name", this.name));
		args.add(new Attribute("new-name", new_name));
		return run(CMD_UPDATE);
	}

	public SSHCommandResult delete() {
		args.clear();
		if (this.Id != null) {
			args.add(new Attribute("id", this.Id));	
		} else {
			args.add(new Attribute("name", this.name));
		}
		return run(CMD_DELETE);
	}

	// ** ** ** ** ** ** **
	// ASSERTS
	// ** ** ** ** ** ** **
}
