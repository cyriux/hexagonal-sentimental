package fr.devoxx.sentimental.infra;

import java.util.Collections;
import java.util.Map;

import fr.devoxx.sentimental.domain.Sentiment;
import fr.devoxx.sentimental.domain.SentimentAnalysis;
import fr.devoxx.sentimental.infra.adapter.CommandLineAdapter;

public class Main {

	public static void main(String[] args) {
		// Lexicon Adapter
		Map<String, Sentiment> dictionary = Collections.singletonMap("Kamoulox", Sentiment.HAPPY);
		
		// Domain Model
		final SentimentAnalysis service = new SentimentAnalysis(dictionary);
		
		// API Adapters
		final CommandLineAdapter adapter = new CommandLineAdapter(service, System.out);
		
		// start
		adapter.adapt(args);
	}

}
