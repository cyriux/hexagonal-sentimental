package fr.devoxx.sentimental.infra.audio;

import java.io.File;

import org.junit.Test;

public class SamplePlayerTest {

	@Test
	public void testPlay() throws Exception {
		final File file = new File("build.wav");
		new SamplePlayer().play(file);
	}

}
