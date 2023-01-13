package com.mycompany.app;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CheckerWorkflow 
{
    @WorkflowMethod
    public void check();

    @QueryMethod
    public int getStatus();
}
