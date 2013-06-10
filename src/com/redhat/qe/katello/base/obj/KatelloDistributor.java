package com.redhat.qe.katello.base.obj;

import javax.management.Attribute;
import com.redhat.qe.tools.SSHCommandResult;

public class KatelloDistributor extends _KatelloObject{

	// ** ** ** ** ** ** ** Public constants
	public static final String CMD_CREATE = "distributor create";
	public static final String CMD_INFO = "distributor info";
	public static final String CMD_ADD_CUSTOM_INFO = "distributor add_custom_info";
	public static final String CMD_REMOVE_CUSTOM_INFO = "distributor remove_custom_info";
	public static final String OUT_CREATE = "Successfully created distributor [ %s ]";
	public static final String OUT_INFO = "Successfully added Custom Information [ %s : %s ] to Distributor [ %s ]";
	public static final String OUT_REMOVE_INFO = "Could not remove Custom Information from Distributor [ %s ]";
	public static final String OUT_REMOVE_INVALID_KEY = "Couldn't find custom info with keyname '%s'";

	// ** ** ** ** ** ** ** Class members
	String org_name;
	String dis_name;
	String uuid;

	public KatelloDistributor(String dOrg,String dName){
		this.org_name = dOrg;
		this.dis_name=dName;
	}

	public SSHCommandResult distributor_create(){
		opts.clear();
		opts.add(new Attribute("org", this.org_name));
		opts.add(new Attribute("name", this.dis_name));
		return run(CMD_CREATE);
	}

	public SSHCommandResult distributor_info(){
		opts.clear();
		opts.add(new Attribute("org", this.org_name));
		opts.add(new Attribute("name", this.dis_name));
		return run(CMD_INFO);
	}
	
	public SSHCommandResult add_info(String keyname,String value,String uuid,String dis_name,String org_name){
		opts.clear();
		opts.add(new Attribute("org",org_name));
		opts.add(new Attribute("name",dis_name));
		opts.add(new Attribute("uuid",uuid));
		opts.add(new Attribute("keyname",keyname));
		opts.add(new Attribute("value",value));
		return run(CMD_ADD_CUSTOM_INFO);		
	}
	
	public SSHCommandResult remove_info(String keyname){
		opts.clear();
		opts.add(new Attribute("org",this.org_name));
		opts.add(new Attribute("name",this.dis_name));
		opts.add(new Attribute("keyname",keyname));
		return run(CMD_REMOVE_CUSTOM_INFO);		
	}
}

