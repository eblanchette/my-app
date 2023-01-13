package com.mycompany.app;

import java.time.Duration;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class App{
    
    
    public static final void main(String [] args){
        System.out.println("Starting");

        WorkflowServiceStubs service;
        WorkflowClient client;
        WorkflowServiceStubsOptions serviceOptions = WorkflowServiceStubsOptions.newBuilder().setTarget("")
                .setRpcTimeout(Duration.ofSeconds(60)).build();
        service = WorkflowServiceStubs.newConnectedServiceStubs(serviceOptions, Duration.ofSeconds(2));
        WorkflowClientOptions clientOptions = WorkflowClientOptions.newBuilder().setNamespace("testing").build();
        client = WorkflowClient.newInstance(service, clientOptions);
        
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker mainWorker = factory.newWorker("testing");

        // Activities
        mainWorker.registerActivitiesImplementations(new CounterActivityImpl());

        // Workflows
        mainWorker.registerWorkflowImplementationTypes(CheckerWorkflowImpl.class);

        factory.start();

        CheckerWorkflow workflow = client.newWorkflowStub(
            CheckerWorkflow.class,
				WorkflowOptions.newBuilder()
						.setWorkflowId("checker")
						.setTaskQueue("testing")
						.build());
        
        workflow.check();
    }

}
