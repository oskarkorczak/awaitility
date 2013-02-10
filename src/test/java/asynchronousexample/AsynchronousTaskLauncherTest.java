package asynchronousexample;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class AsynchronousTaskLauncherTest {

	@Mock private ExecutorService executorService;
	@Mock private DelayedFileCreator delayedFileCreatorTask;
	@InjectMocks private AsynchronousTaskLauncher asynchronousTaskLauncher;

	@BeforeMethod
	public void prepareMocks() {
		initMocks(this);
		asynchronousTaskLauncher = new AsynchronousTaskLauncher(executorService);
	}

	@Test
	public void shouldRunATaskInNewThread() {
		asynchronousTaskLauncher.launch(delayedFileCreatorTask);

		verify(executorService).execute(delayedFileCreatorTask);
		verify(executorService).shutdown();
	}
}
