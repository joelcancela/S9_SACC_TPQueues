package fr.unice.polytech.si5.cc.jcancela;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PullQueueTaskCreator {
	void createTask() {
		Queue q = QueueFactory.getQueue("my-pull-queue");
		q.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload("Something").tag("Noob".getBytes()));
	}

	//TODO: how to specify worker ?
	void workerWorksOnTasks(){
		Queue q = QueueFactory.getQueue("my-pull-queue");
		long numberOfTasksToLease = 4;
		List<TaskHandle> tasks = q.leaseTasksByTag(3600, TimeUnit.SECONDS, numberOfTasksToLease, "Noob");
		for(TaskHandle task: tasks){
			task.getName();//WHAT TO DO
			q.deleteTask(task);
		}
	}
}
