package fr.devoxx.sentimental.domain;

import java.util.Map;

import fr.devoxx.sentimental.annotation.NotThereYet;

@NotThereYet
public class InMemoryLexicon implements Lexicon {
	private Map<String, Sentiment> dictionary;

	public InMemoryLexicon(Map<String, Sentiment> dictionary) {
		this.dictionary = dictionary;
	}

	public Sentiment get(String key) {
		return dictionary.get(key);
	}

}