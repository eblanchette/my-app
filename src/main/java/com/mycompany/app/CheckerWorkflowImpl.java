package com.mycompany.app;

import java.time.Duration;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

public class CheckerWorkflowImpl implements CheckerWorkflow
{
    private CounterActivity counterActivity;
    private int status = -1;

    public CheckerWorkflowImpl(){
        this.counterActivity = Workflow.newActivityStub(CounterActivity.class,
        ActivityOptions.newBuilder()
                        .setStartToCloseTimeout(Duration.ofHours(1))
                        .build());
    }

    @Override
    public void check() {

        String runId = Workflow.getInfo().getRunId();
        String workflowId = Workflow.getInfo().getWorkflowId();
        status = counterActivity.count();

        Workflow.getLogger(CheckerWorkflowImpl.class).info("workflowId:" + workflowId + " runId:" + runId, " status:" + status);

        // Wait 24 hours before checking again
        Workflow.sleep(Duration.ofHours(24));

        // Trigger new Workflow
        Workflow.continueAsNew();

        return;
        
    }

    public int getStatus() {
        return status;
    }
}
