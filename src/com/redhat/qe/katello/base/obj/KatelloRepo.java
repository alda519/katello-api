package com.redhat.qe.katello.base.obj;

import java.util.ArrayList;

import javax.management.Attribute;

import com.redhat.qe.auto.testng.Assert;
import com.redhat.qe.katello.base.KatelloCli;
import com.redhat.qe.katello.tasks.KatelloTasks;
import com.redhat.qe.tools.SSHCommandResult;

public class KatelloRepo {
	
	// ** ** ** ** ** ** ** Public constants
	// Red Hat Enterprise Linux 6 Server RPMs x86_64 6Server
	public static final String RH_REPO_PRODUCT_VER = "6Server";
	public static final String RH_REPO_RHEL6_SERVER_RPMS_64BIT = 
			"Red Hat Enterprise Linux 6 Server RPMs x86_64 "+RH_REPO_PRODUCT_VER;
	
	public static final String CMD_CREATE = "repo create";
	public static final String CMD_SYNCHRONIZE = "repo synchronize";
	public static final String CMD_UPDATE = "repo update";
	public static final String CMD_INFO = "repo info";
	public static final String CMD_ENABLE = "repo enable";
	public static final String CMD_ADD_FILTER = "repo add_filter";
	public static final String CMD_STATUS = "repo status";
	public static final String CMD_LIST = "repo list -v";
	
	public static final String OUT_CREATE = 
			"Successfully created repository [ %s ]"; 

	// ** ** ** ** ** ** ** Class members
	public String name;
	public String org;
	public String product;
	public String url;
	public String gpgkey;
	public boolean nogpgkey = false;
	
	private KatelloCli cli;
	private ArrayList<Attribute> opts;
	
	public KatelloRepo(String pName, String pOrg, 
			String pProd, String pUrl, 
			String pGpgkey, Boolean pNogpgkey){
		this.name = pName;
		this.org = pOrg;
		this.product = pProd;
		this.url = pUrl;
		this.gpgkey = pGpgkey;
		if(pNogpgkey != null)
			this.nogpgkey = pNogpgkey.booleanValue();
		this.opts = new ArrayList<Attribute>();
	}
	
	public SSHCommandResult create(){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("product", product));
		opts.add(new Attribute("url", url));
		opts.add(new Attribute("gpgkey", gpgkey));
		if(nogpgkey)
			opts.add(new Attribute("nogpgkey", ""));
		cli = new KatelloCli(CMD_CREATE, opts);
		return cli.run();
	}
	
	public SSHCommandResult synchronize(){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("product", product));
		cli = new KatelloCli(CMD_SYNCHRONIZE, opts);
		return cli.run();
	}
	
	public SSHCommandResult update_gpgkey(){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("product", product));
		opts.add(new Attribute("gpgkey", gpgkey));
		cli = new KatelloCli(CMD_UPDATE, opts);
		return cli.run();
	}
	
	public SSHCommandResult info(){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("product", product));
		cli = new KatelloCli(CMD_INFO, opts);
		return cli.run();
	}
	
	public SSHCommandResult info(String environment){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("product", product));
		opts.add(new Attribute("environment", environment));
		cli = new KatelloCli(CMD_INFO, opts);
		return cli.run();
	}

	public SSHCommandResult enable(){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("product", product));
		cli = new KatelloCli(CMD_ENABLE, opts);
		return cli.run();
	}
	
	public SSHCommandResult add_filter(String filter){
		opts.clear();
		opts.add(new Attribute("filter", filter));
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("product", product));
		cli = new KatelloCli(CMD_ADD_FILTER, opts);
		return cli.run();
	}
	
	public SSHCommandResult status(){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("product", product));
		cli = new KatelloCli(CMD_STATUS, opts);
		return cli.run();
	}

	public SSHCommandResult status(String environment){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("name", name));
		opts.add(new Attribute("product", product));
		opts.add(new Attribute("environment", environment));
		cli = new KatelloCli(CMD_STATUS, opts);
		return cli.run();
	}

	public SSHCommandResult list(){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("product", product));
		cli = new KatelloCli(CMD_LIST, opts);
		return cli.run();
	}

	public SSHCommandResult list(String environment){
		opts.clear();
		opts.add(new Attribute("org", org));
		opts.add(new Attribute("environment", environment));
		cli = new KatelloCli(CMD_LIST, opts);
		return cli.run();
	}

	// ** ** ** ** ** ** **
	// ASSERTS
	// ** ** ** ** ** ** **
	
	public void assert_repoHasGpg(){
		SSHCommandResult res;
		
		res = info();
		Assert.assertTrue(res.getExitCode().intValue()==0, "Check - return code (repo info)");
		String gpg_key = KatelloTasks.grepCLIOutput("GPG key", res.getStdout());
		Assert.assertTrue(this.gpgkey.equals(gpg_key), 
				String.format("Check - GPG key [%s] should be found in the repo info",this.gpgkey));
		KatelloGpgKey gpg = new KatelloGpgKey(this.gpgkey, this.org, null);
		res = gpg.info();
		Assert.assertTrue(res.getExitCode().intValue()==0, "Check - return code (gpg_key info)");
		String reposWithGpg = KatelloTasks.grepCLIOutput("Repositories", res.getStdout());
		Assert.assertTrue(reposWithGpg.contains(this.name), 
				"Check - Repo should be in repositories list of GPG key");
	}
	
}