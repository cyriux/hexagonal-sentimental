package fr.devoxx.sentimental.domain;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import fr.devoxx.sentimental.domain.Sentiment;
import fr.devoxx.sentimental.domain.SentimentAnalysis;

public class SentimentAnalysisTest {

	@Test
	public void should_return_neutral_by_default() {
		final Map<String, Sentiment> dictionary = Collections.emptyMap();
		final SentimentAnalysis sa = new SentimentAnalysis(dictionary);
		assertEquals(Sentiment.NEUTRAL, sa.sentimentOf("Whazzup!!!"));
	}

	@Test
	// Given sad is related to the sentiment SAD,
	public void should_recognize_sadness_when_the_word_sad_is_in_the_sentence() {
		final Map<String, Sentiment> dictionary = Collections.singletonMap("sad", Sentiment.SAD);
		final SentimentAnalysis sa = new SentimentAnalysis(dictionary);
		assertEquals(Sentiment.SAD, sa.sentimentOf("It's sad js rulz the web world"));
	}

	@Test
	// Given 'wifi' is related to the sentiment HAPPY,
	public void should_recognize_happiness_when_the_word_wifi_is_in_the_sentence() {
		final Map<String, Sentiment> dictionary = Collections.singletonMap("wifi", Sentiment.HAPPY);
		final SentimentAnalysis sa = new SentimentAnalysis(dictionary);
		assertEquals(Sentiment.HAPPY, sa.sentimentOf("There's free wifi here"));
	}

	@Test
	public void should_recognize_whatever_the_case_of_the_sentence() {
		final Map<String, Sentiment> dictionary = Collections.singletonMap("wifi", Sentiment.HAPPY);
		final SentimentAnalysis sa = new SentimentAnalysis(dictionary);
		assertEquals(Sentiment.HAPPY, sa.sentimentOf("There's free WIFI here"));
	}

	@Test
	public void should_recognize_negation_of_sentiment() {
		final Map<String, Sentiment> dictionary = Collections.singletonMap("wifi", Sentiment.HAPPY);
		final SentimentAnalysis sa = new SentimentAnalysis(dictionary);
		assertEquals(Sentiment.SAD, sa.sentimentOf("There's no WIFI"));
	}

	@Test
	public void should_recognize_double_negation_of_sentiment() {
		final Map<String, Sentiment> dictionary = Collections.singletonMap("wifi", Sentiment.HAPPY);
		final SentimentAnalysis sa = new SentimentAnalysis(dictionary);
		assertEquals(Sentiment.HAPPY, sa.sentimentOf("It's not true there's no WIFI"));
	}

	@Test
	public void should_pick_the_most_optimistic_in_case_of_conflict() {
		final Map<String, Sentiment> dictionary = new HashMap<String, Sentiment>();
		dictionary.put("kitten", Sentiment.HAPPY);
		dictionary.put("drunk", Sentiment.SAD);
		final SentimentAnalysis sa = new SentimentAnalysis(dictionary);
		assertEquals(Sentiment.HAPPY, sa.sentimentOf("Oh, look! A drunk kitten"));
	}
	
	@Test
	public void actual_failing_case() {
		final Map<String, Sentiment> dictionary = new HashMap<String, Sentiment>();
		dictionary.put("devoxx", Sentiment.HAPPY);
		dictionary.put("spring", Sentiment.SAD);
		final SentimentAnalysis sa = new SentimentAnalysis(dictionary);
		assertEquals(Sentiment.SAD, sa.sentimentOf("@tpierrain #devoxx spring"));
	}

}
