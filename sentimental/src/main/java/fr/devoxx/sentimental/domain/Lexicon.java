package fr.devoxx.sentimental.domain;


public interface Lexicon {

	Sentiment get(String key);

}