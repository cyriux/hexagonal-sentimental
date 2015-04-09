package fr.devoxx.sentimental.infra.audio;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class SamplePlayer {

	public void play(File file) {
		try {
			AudioInputStream stream;
			AudioFormat format;
			DataLine.Info info;
			final Clip clip;

			stream = AudioSystem.getAudioInputStream(file);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);

			clip.addLineListener(new LineListener() {
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP)
						clip.close();
				}
			});
			clip.open(stream);
			clip.start();
		} catch (Exception e) {
			throw new RuntimeException(e.getClass().getSimpleName() + " " + e.getMessage());
		}
	}
}
