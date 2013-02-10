package asynchronousexample;

import java.util.concurrent.ExecutorService;

public class AsynchronousTaskLauncher {

	private ExecutorService executorService;

	public AsynchronousTaskLauncher(ExecutorService anExecutorService) {
		this.executorService = anExecutorService;
	}

	public void launch(Runnable task) {
		executorService.execute(task);
		executorService.shutdown();
	}
}
