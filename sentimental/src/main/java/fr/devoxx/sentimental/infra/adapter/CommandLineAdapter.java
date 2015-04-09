package fr.devoxx.sentimental.infra.adapter;

import java.io.PrintStream;

import fr.devoxx.sentimental.domain.Sentiment;
import fr.devoxx.sentimental.domain.SentimentAnalysis;

/**
 * Adapter from args to 
 */
public class CommandLineAdapter {

	private final SentimentAnalysis analysis;
	private final PrintStream sentimentPublisher;

	public CommandLineAdapter(SentimentAnalysis analysis, PrintStream sentimentPublisher) {
		this.analysis = analysis;
		this.sentimentPublisher = sentimentPublisher;
	}

	public void adapt(String[] args) {
		for (String sentence : args) {
			final Sentiment sentiment = analysis.sentimentOf(sentence);
			sentimentPublisher.println(sentiment.toString());
		}
	}

	
}
