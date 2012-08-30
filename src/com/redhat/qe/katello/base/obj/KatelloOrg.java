package com.redhat.qe.katello.base.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.management.Attribute;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.json.simple.JSONObject;

import com.redhat.qe.katello.base.KatelloApi;
import com.redhat.qe.katello.base.KatelloApiException;
import com.redhat.qe.katello.base.KatelloApiResponse;
import com.redhat.qe.katello.base.KatelloCli;
import com.redhat.qe.katello.base.KatelloTestScript;
import com.redhat.qe.tools.SSHCommandResult;

@JsonIgnoreProperties(ignoreUnknown=true)
public class KatelloOrg {
    protected static Logger log = Logger.getLogger(KatelloOrg.class.getName());

	// ** ** ** ** ** ** ** Public constants
	public static final String DEFAULT_ORG = "ACME_Corporation";
	
	public static final String CLI_CMD_CREATE = "org create";
	public static final String CLI_CMD_INFO = "org info";
	public static final String CLI_CMD_LIST = "org list";
	public static final String CMD_SUBSCRIPTIONS = "org subscriptions";
	public static final String CMD_UEBERCERT = "org uebercert";
	public static final String CMD_DELETE = "org delete";
	public static final String CMD_UPDATE = "org update";
	
//	public static final String API_CMD_CREATE = "/organizations";
//	public static final String API_CMD_LIST = "/organizations";
	public static final String API_CMD_INFO = "/organizations/%s";
	
	public static final String ERR_TEMPLATE_NOTFOUND = 
			"Could not find template [ %s ]";	
	public static final String OUT_CREATE = 
			"Successfully created org [ %s ]";
	public static final String ERR_ORG_EXISTS = 
			"Validation failed: Name has already been taken";
	public static final String ERR_NAME_INVALID = 
			"Validation failed: Cp key is invalid, Name cannot contain characters other than alpha numerals, space,'_', '-'.";
	public static final String ERR_ORG_NOTFOUND = 
			"Couldn't find organization '%s'";
	
	public static final String REG_ORG_LIST = ".*Id:\\s+\\d+.*Name:\\s+%s.*Description:\\s+%s.*";
	public static final String REG_ORG_INFO = ".*Id:\\s+\\d+.*Name:\\s+%s.*Description:.*%s.*";
	
	public static final String OUT_ORG_SUBSCR = "Subscription:   %s";
	
	// ** ** ** ** ** ** ** Class members
	public String name;
	public String description;
	private Long id;
	private String cpKey;
	
	private KatelloCli cli;
	private ArrayList<Attribute> opts;
	
	public KatelloOrg() {	    
	}
	
	public KatelloOrg(String pName, String pDesc){
		this.name = pName;
		this.description = pDesc;
		this.opts = new ArrayList<Attribute>();
	}
	
	protected KatelloOrg(Long id, String name, String description) {
	    this(name, description);
	    this.id = id;
	}
	
	public Long getId() {
	    return id;
	}
	
	public void setId(Long id) {
	    this.id = id;
	}
	
	public String getName() {
	    return name;
	}
	
	public void setName(String name) {
	    this.name = name;
	}
	
	@JsonProperty("cp_key")
	public String getCpKey() {
	    return cpKey;
	}

	@JsonProperty("cp_key")
	public void setCpKey(String cpKey) {
	    this.cpKey = cpKey;
	}
	
	public SSHCommandResult cli_create(){		
		return cli_create(null);
	}
	
	public SSHCommandResult cli_create(KatelloUser user){		
		opts.clear();
		opts.add(new Attribute("name", this.name));
		opts.add(new Attribute("description", this.description));
		if (user == null) { 
			cli = new KatelloCli(CLI_CMD_CREATE, opts);
		} else {
			cli = new KatelloCli(CLI_CMD_CREATE, opts, user);
		}	
		return cli.run();
	}
	
	public SSHCommandResult cli_info(){
		opts.clear();
		opts.add(new Attribute("name", this.name));
		cli = new KatelloCli(CLI_CMD_INFO, opts);
		return cli.run();
	}
	
	public SSHCommandResult cli_list(){
		opts.clear();
		KatelloCli cli = new KatelloCli(CLI_CMD_LIST+" -v", opts);
		return cli.run();
	}
		
	public SSHCommandResult subscriptions(){
		opts.clear();
		opts.add(new Attribute("name", this.name));
		cli = new KatelloCli(CMD_SUBSCRIPTIONS, opts);
		return cli.run();
	}

	public SSHCommandResult uebercert(){
		opts.clear();
		opts.add(new Attribute("name", this.name));
		cli = new KatelloCli(CMD_UEBERCERT, opts);
		return cli.run();
	}

	public SSHCommandResult delete(){
		opts.clear();
		opts.add(new Attribute("name", this.name));
		cli = new KatelloCli(CMD_DELETE, opts);
		return cli.run();
	}
	
	public SSHCommandResult update(String new_description){
		opts.clear();
		opts.add(new Attribute("name", this.name));
		opts.add(new Attribute("description", new_description));
		cli = new KatelloCli(CMD_UPDATE, opts);
		return cli.run();
	}

	// ** ** ** ** ** ** **
	// ASSERTS
	// ** ** ** ** ** ** **
}
