package asynchronousexample;

import java.io.File;
import java.io.IOException;

public class DelayedFileCreator implements Runnable {

	private static final int THREE_SECONDS = 3000;

	private File file;
	private Timer timer;

	public DelayedFileCreator(Timer aTimer, File aFile) {
		timer = aTimer;
		file = aFile;
	}

	@Override
	public void run() {
		sleepBeforeCreatingFile();
		createNewFile();
	}

	private void sleepBeforeCreatingFile() {
		try {
			timer.sleep(THREE_SECONDS);
		} catch (InterruptedException ie) {
			throw new RuntimeException(ie);
		}
	}

	private void createNewFile() {
		try {
			file.createNewFile();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
}