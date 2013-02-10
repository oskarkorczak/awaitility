package asynchronousexample;

import com.googlecode.catchexception.CatchException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static com.googlecode.catchexception.apis.CatchExceptionBdd.when;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasNoCause;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class DelayedFileCreatorTest {

	@Mock private File file;
	@Mock private Timer timer;
	@InjectMocks private DelayedFileCreator delayedFileCreator;


	@BeforeMethod
	public void prepareMocks() {
		initMocks(this);
		delayedFileCreator = new DelayedFileCreator(timer, file);
	}

	@Test
	public void shouldImplementRunnableInterface() {
		assertThat(delayedFileCreator instanceof Runnable, is(equalTo(true)));
	}

	@Test
	public void shouldWaitThreeSecondsBeforeCreatingFile() throws IOException, InterruptedException {
		int threeSeconds = 3000;

		delayedFileCreator.run();

		InOrder inOrder = inOrder(timer, file);
		inOrder.verify(timer).sleep(threeSeconds);
		inOrder.verify(file).createNewFile();
		verifyNoMoreInteractions(timer, file);
	}

	@Test
	public void shouldHandleInterruptedException() throws InterruptedException {
		doThrow(new RuntimeException()).when(timer).sleep(anyInt());

		when(delayedFileCreator).run();

		assertThatExceptionIsRuntimeExceptionWithNoCause();
	}

	@Test
	public void shouldHandleIOException() throws IOException {
		doThrow(new RuntimeException()).when(file).createNewFile();

		when(delayedFileCreator).run();

		assertThatExceptionIsRuntimeExceptionWithNoCause();
	}

	private void assertThatExceptionIsRuntimeExceptionWithNoCause() {
		assertThat(CatchException.<RuntimeException>caughtException(),
				allOf(
						is(RuntimeException.class),
						hasNoCause()
				)
		);
	}
}
