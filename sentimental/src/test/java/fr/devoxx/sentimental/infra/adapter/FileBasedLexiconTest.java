package fr.devoxx.sentimental.infra.adapter;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.devoxx.sentimental.domain.Sentiment;

public class FileBasedLexiconTest {

	@Test
	public void testLexicon() throws Exception {
		final FileBasedLexicon lexicon = new FileBasedLexicon("lexicon.properties");
		assertEquals(Sentiment.HAPPY, lexicon.get("devoxx"));
		assertEquals(Sentiment.SAD, lexicon.get("framework"));
	}
}
