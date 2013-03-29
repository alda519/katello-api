package com.redhat.qe.katello.base.obj;

import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonProperty;
import org.json.simple.JSONObject;
import javax.management.Attribute;
import com.redhat.qe.tools.SSHCommandResult;

public class KatelloTask extends _KatelloObject{
	protected static Logger log = Logger.getLogger(KatelloTask.class.getName());

	public static final String CMD_TASKSTATUS = "task status";

	@JsonProperty("task_type")
	public String task_type;
	
	@JsonProperty("task_owner_id")
	public String task_owner_id;
	
	@JsonProperty("organization_id")
	public String organization_id;
	
	@JsonProperty("user_id")
	public Long user_id;

	@JsonProperty("finish_time")
	public String finish_time;
	
	@JsonProperty("start_time")
	public String start_time;

	@JsonProperty("pending")
	public Boolean pending;

	@JsonProperty("uuid")
	public String uuid;
	
	@JsonProperty("updated_at")
	public String updated_at;

	@JsonProperty("state")
	public String state;

	@JsonProperty("parameters")
	public Object parameters;

	@JsonProperty("result")
	public JSONObject result;
	
	@JsonProperty("created_at")
	public String created_at;
	
	@JsonProperty("task_owner_type")
	public String task_owner_type;
	
	@JsonProperty("progress")
	public JSONObject progress;
	
	@JsonProperty("id")
	public Long id;
	
	public KatelloTask(){super();}
	
	public KatelloTask(String uuid) {
		this.uuid = uuid;
	}

	public SSHCommandResult task_status() {
		opts.clear();
		opts.add(new Attribute("uuid", this.uuid));
		return run(CMD_TASKSTATUS);
	}
}
