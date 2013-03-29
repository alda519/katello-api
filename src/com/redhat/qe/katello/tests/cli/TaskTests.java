package com.redhat.qe.katello.tests.cli;

import org.testng.annotations.Test;

import com.redhat.qe.Assert;
import com.redhat.qe.katello.base.KatelloCliTestScript;
import com.redhat.qe.katello.base.obj.KatelloTask;
import com.redhat.qe.tools.SSHCommandResult;

public class TaskTests extends KatelloCliTestScript {

	@Test(description="task status that does not exist")
    public void test_task_status() {
		SSHCommandResult res;
        KatelloTask task = new KatelloTask("fooo");
        res = task.task_status();
        Assert.assertContainsNoMatch(res.getStderr(), "undefined method"); // bug 929106
	}

}
