package fr.devoxx.sentimental.infra.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import fr.devoxx.sentimental.domain.Lexicon;
import fr.devoxx.sentimental.domain.Sentiment;

public class FileBasedLexicon implements Lexicon {

	private final Properties properties = new Properties();

	/**
	 * @param fileName
	 *            dot-separated, e.g. "config.properties"
	 */
	public FileBasedLexicon(String fileName) {
		final InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		if (is != null) {
			try {
				properties.load(is);
			} catch (IOException e) {
				throw new RuntimeException("Could not load property file '" + fileName);
			}
		} else {
			throw new RuntimeException("property file '" + fileName + "' not found in the classpath");
		}
	}

	public Sentiment get(String key) {
		final String value = properties.getProperty(key);
		return value == null ? Sentiment.NEUTRAL : Sentiment.valueOf(value);
	}

	@Override
	public String toString() {
		return "File-Based Lexicon: " + properties.size() + " entries";
	}
}
