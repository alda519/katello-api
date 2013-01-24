package com.redhat.qe.katello.base.obj;

import javax.management.Attribute;
import com.redhat.qe.Assert;
import com.redhat.qe.katello.base.KatelloCli;
import com.redhat.qe.katello.base.KatelloCliTestScript;
import com.redhat.qe.tools.SSHCommandResult;

public class KatelloActivationKey extends _KatelloObject{

	String org;
	String environment;
	String name;
	String description;
	String template;
	String limit;
	
	private String id;
	private String environment_id;
	private String template_id;
	
	public static final String CMD_CREATE = "activation_key create";
	public static final String CMD_INFO = "activation_key info";
	public static final String CMD_LIST = "activation_key list";
	public static final String CMD_DELETE = "activation_key delete";
	public static final String CMD_UPDATE = "activation_key update";
	public static final String CMD_ADD_SYSTEMGROUP = "activation_key add_system_group";
	public static final String CMD_REMOVE_SYSTEMGROUP = "activation_key remove_system_group";
	public static final String ERR_TEMPLATE_NOTFOUND = 
			"Could not find template [ %s ]";	
	public static final String OUT_CREATE = 
			"Successfully created activation key [ %s ]";
	public static final String OUT_DELETE =
			"Successfully deleted activation key [ %s ]";
	public static final String OUT_ADD_SYSTEMGROUP = 
			"Successfully added system group to activation key [ %s ]";
	public static final String OUT_REMOVE_SYSTEMGROUP = 
			"Successfully removed system group from activation key [ %s ]";
	public static final String ERROR_INFO =
			"Could not find activation key [ %s ]";
	public static final String ERROR_EXCEED =
			"Usage limit (%s) exhausted for activation key '%s'";	
	
	public KatelloActivationKey(String pOrg, String pEnv, String pName, String pDesc, String pTemplate){
		this(pOrg,pEnv,pName,pDesc,pTemplate,null);
		
	}
	
	public KatelloActivationKey(String pOrg, String pEnv, String pName, String pDesc, String pTemplate, String pLimit){
		this.org = pOrg;
		this.environment = pEnv;
		this.name = pName;
		this.description = pDesc;
		this.template = pTemplate;
		this.limit = pLimit;
	}
		
	public SSHCommandResult create(){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("environment", environment));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("description", description));
		opts.add(new Attribute("template", template));
		opts.add(new Attribute("limit", limit));
		return run(CMD_CREATE);
	}
	
	public SSHCommandResult info(){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		return run(CMD_INFO);
	}
	
	public SSHCommandResult list(){
		opts.clear();
		opts.add(new Attribute("org", org));
		return run(CMD_LIST+" -v");
	}

	public SSHCommandResult extend_limit(String newlimit){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("limit", newlimit));
		return run(CMD_UPDATE);
	}
	
	public SSHCommandResult add_system_group(String pSystemGroup){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("system_group", pSystemGroup));
		return run(CMD_ADD_SYSTEMGROUP);
	}
	
	public SSHCommandResult update_add_subscription(String subscriptionId){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("add_subscription", subscriptionId));
		return run(CMD_UPDATE);
	}

	public SSHCommandResult update_remove_subscription(String subscriptionId){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("remove_subscription", subscriptionId));
		return run(CMD_UPDATE);
	}

	public SSHCommandResult remove_system_group(String pSystemGroup){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("system_group", pSystemGroup));
		return run(CMD_REMOVE_SYSTEMGROUP);
	}
	
	public SSHCommandResult delete(){
		   opts.clear();
		   opts.add(new Attribute("name",name));
		   opts.add(new Attribute("org",org));
		   return run(CMD_DELETE+" -v");
		    
	}
	
	// ** ** ** ** ** ** **
	// ASSERTS
	// ** ** ** ** ** ** **
	public void asserts_create(){
		SSHCommandResult res;
		if(this.id==null)
			updateIDs();
		
		// asserts: activation_key list
		res = list(); // if environment != null; result will be returned by that env. only 
		Assert.assertTrue(res.getExitCode().intValue()==0, "Check - return code (activation_key list)");
		String REGEXP_AK_LIST = ".*Id\\s*:\\s*\\d+.*Name\\s*:\\s*%s.*Environment Id\\s*:\\s*%s.*System Template Id\\s*:\\s*%s.*";
		
		String match_info = String.format(REGEXP_AK_LIST,
				this.name,this.environment_id,this.template_id).replaceAll("\"", "");
		if(this.template_id==null){
			match_info = String.format(REGEXP_AK_LIST,
					this.name,this.environment_id,"None").replaceAll("\"", "");
		}
		Assert.assertTrue(KatelloCliTestScript.sgetOutput(res).replaceAll("\n", "").matches(match_info), 
				String.format("Activation key [%s] should be found in the list",this.name));
		
		// asserts: activation_key info
		res = info();
		Assert.assertTrue(res.getExitCode().intValue()==0, "Check - return code (activation_key info)");
		String REGEXP_AK_INFO = ".*Id\\s*:\\s*\\d+.*Name\\s*:\\s*%s.*Environment Id\\s*:\\s*%s.*System Template Id\\s*:\\s*%s.*Pools\\s*:.*";		
		match_info = String.format(REGEXP_AK_INFO,
				this.name, this.environment_id,this.template_id).replaceAll("\"", "");
		if(this.template_id==null){
			match_info = String.format(REGEXP_AK_INFO,
					this.name,this.environment_id,"None").replaceAll("\"", "");				
		}
		Assert.assertTrue(KatelloCliTestScript.sgetOutput(res).replaceAll("\n", "").matches(match_info), 
				String.format("Activation key [%s] should contain correct info",this.name));			
	}
	
	/**
	 * Retrieves the IDs (or does updates) like:<BR>
	 * id - activation key id in DB<BR>
	 * environment_id - id of the environment<BR>
	 * template_id - id of the template (could be null)<BR>
	 * subscriptions - array of pool_ids (could be null) 
	 */
	private void updateIDs(){
		SSHCommandResult res;
		// retrieve environment_id
		if(this.environment != null){
			KatelloEnvironment env = new KatelloEnvironment(this.environment, null, this.org, KatelloEnvironment.LIBRARY);
			res = env.cli_info();
			Assert.assertTrue(res.getExitCode().intValue()==0, "Check - return code (environment info)");
			this.environment_id = KatelloCli.grepCLIOutput("Id", res.getStdout());				
		}
		//retrieve template_id for an environment
		if(this.template !=null){
			KatelloTemplate tmpl = new KatelloTemplate(template, null, this.org, null);
			res = tmpl.info(this.environment);
			Assert.assertTrue(res.getExitCode().intValue()==0, "Check - return code (template info)");
			this.template_id = KatelloCli.grepCLIOutput("Id", res.getStdout());				
		}
		// retrieve id
		if(this.name != null){
			res = info();
			this.id = KatelloCli.grepCLIOutput("Id", res.getStdout());
		}
	}
}
