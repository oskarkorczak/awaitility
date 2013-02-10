package integration;

import asynchronousexample.AsynchronousTaskLauncher;
import asynchronousexample.DelayedFileCreator;
import asynchronousexample.Timer;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jayway.awaitility.Awaitility.with;
import static com.jayway.awaitility.Duration.ONE_HUNDRED_MILLISECONDS;
import static com.jayway.awaitility.Duration.TEN_SECONDS;
import static com.jayway.awaitility.Duration.TWO_HUNDRED_MILLISECONDS;
import static org.hamcrest.Matchers.equalTo;

public class CreateFileAsynchronouslyIntegrationTest {

	private static final int THREAD_POOL_SIZE = 3;
	private static final String FILENAME = "sample.txt";

	@BeforeTest
	public void deleteFileFromFileSystem() {
		File file = new File(FILENAME);
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void shouldAsynchronouslyWriteFileOnDisk() throws Exception {
		AsynchronousTaskLauncher launcher = prepareAsynchronousTaskLauncher();
		Runnable delayedFileCreatorTask = prepareDelayedFileCreatorWith(FILENAME);

		launcher.launch(delayedFileCreatorTask);

		with().pollDelay(ONE_HUNDRED_MILLISECONDS)
				.and().with().pollInterval(TWO_HUNDRED_MILLISECONDS)
				.and().with().timeout(TEN_SECONDS)
				.await("file creation")
				.until(fileIsCreatedOnDisk(FILENAME), equalTo(true));
	}

	private Runnable prepareDelayedFileCreatorWith(String filename) {
		Timer timer = new Timer();
		File file = new File(filename);
		return new DelayedFileCreator(timer, file);
	}

	private AsynchronousTaskLauncher prepareAsynchronousTaskLauncher() {
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		return new AsynchronousTaskLauncher(executorService);
	}

	private Callable<Boolean> fileIsCreatedOnDisk(final String filename) {
		return new Callable<Boolean>() {
			public Boolean call() throws Exception {
				File file = new File(filename);
				return file.exists();
			}
		};
	}
}
