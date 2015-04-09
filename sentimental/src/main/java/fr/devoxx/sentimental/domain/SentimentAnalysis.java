package fr.devoxx.sentimental.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import fr.devoxx.sentimental.annotation.DomainService;

/**
 * Sentiment Analysis is about extracting the sentiment (mood) from a given
 * sentence.
 */
@DomainService
public class SentimentAnalysis {

	private final Lexicon lexicon;
	private final Trend audit;

	public SentimentAnalysis(Lexicon lexicon, Trend audit) {
		this.lexicon = lexicon;
		this.audit = audit;
	}

	public SentimentAnalysis(Map<String, Sentiment> dictionary, Trend audit) {
		this(new InMemoryLexicon(dictionary), audit);
	}

	public SentimentAnalysis(Map<String, Sentiment> dictionary) {
		this(new InMemoryLexicon(dictionary), null);
	}

	public Sentiment sentimentOf(String sentence) {
		final Sentiment sentiment = analyze(sentence);
		if (audit != null) {
			audit.record(sentence, sentiment);
		}
		return sentiment;
	}

	private Sentiment analyze(String sentence) {
		final String[] words = sentence.toLowerCase().split(" ");
		int negationCount = 0;
		final List<Sentiment> sentiments = new ArrayList<Sentiment>();
		for (String word : words) {
			if (word.equals("no") || word.equals("not")) {
				negationCount++;
			}
			final Sentiment sentiment = lexicon.get(word);
			if (sentiment != null) {
				sentiments.add(sentiment);
			}
		}
		if (sentiments.isEmpty()) {
			return Sentiment.NEUTRAL;
		}
		Collections.sort(sentiments);
		final Sentiment sentiment = mostOptimistic(sentiments);
		return isNegation(negationCount) ? sentiment.opposite() : sentiment;
	}

	protected boolean isNegation(int negationCount) {
		return negationCount % 2 != 0;
	}

	private Sentiment mostOptimistic(final List<Sentiment> sentiments) {
		return sentiments.get(sentiments.size() - 1);
	}

}
