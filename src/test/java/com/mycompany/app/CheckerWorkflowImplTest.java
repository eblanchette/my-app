package com.mycompany.app;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;

public class CheckerWorkflowImplTest 
{
    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient client;

    private String TASK_QUEUE = "test-task-queue";

    // Set up the test workflow environment
    @Before
    public void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker(TASK_QUEUE);
        // Register your workflow implementations
        worker.registerWorkflowImplementationTypes(CheckerWorkflowImpl.class);

        client = testEnv.getWorkflowClient();
    }

    // Clean up test environment after tests are completed
    @After
    public void tearDown() {
        if(testEnv!=null){
            testEnv.close();
        }
    }

    @Test
    public void testMockedDetail() {

        CounterActivityImpl counterActivity = new CounterActivityImpl();
        assertEquals(0, counterActivity.count());
        assertEquals(1, counterActivity.count());
        assertEquals(2, counterActivity.count());
        assertEquals(3, counterActivity.count());

        worker.registerActivitiesImplementations(counterActivity);

        // Start test environment
        testEnv.start();

        String WORKFLOW_ID = "test-workflow-id";

        // Create the workflow stub
        CheckerWorkflow workflow = client.newWorkflowStub(
            CheckerWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(TASK_QUEUE).setWorkflowId(WORKFLOW_ID).build());

        WorkflowExecution execution = WorkflowClient.start(workflow::check);
        assertEquals(WORKFLOW_ID, execution.getWorkflowId());

        testEnv.sleep(Duration.ofHours(1));
        assertEquals(4, workflow.getStatus());

        testEnv.sleep(Duration.ofDays(1));
        assertEquals(5, workflow.getStatus());

        testEnv.sleep(Duration.ofDays(1));
        assertEquals(6, workflow.getStatus());

        testEnv.sleep(Duration.ofDays(1));
        assertEquals(7, workflow.getStatus());

        testEnv.sleep(Duration.ofDays(1));
        assertEquals(8, workflow.getStatus());


    }




    
}
