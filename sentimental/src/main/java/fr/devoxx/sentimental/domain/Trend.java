package fr.devoxx.sentimental.domain;


public interface Trend {

	void record(String sentence, Sentiment sentiment);
}
