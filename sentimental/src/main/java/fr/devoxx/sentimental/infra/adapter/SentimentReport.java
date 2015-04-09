package fr.devoxx.sentimental.infra.adapter;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.devoxx.sentimental.annotation.DataTransferObject;

@DataTransferObject
public class SentimentReport {

	@Length(max = 140)
	private String sentence;

	@Length(max = 24)
	private String sentiment;

	public SentimentReport() {
		// Jackson deserialization
	}

	public SentimentReport(String sentence, String sentiment) {
		this.sentence = sentence;
		this.sentiment = sentiment;
	}

	@JsonProperty
	public String getSentence() {
		return sentence;
	}

	@JsonProperty
	public String getSentiment() {
		return sentiment;
	}

	@Override
	public int hashCode() {
		return 31 ^ sentence.hashCode() + sentiment.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SentimentReport)) {
			return false;
		}
		SentimentReport other = (SentimentReport) obj;
		return sentence.equals(other.sentence) && sentiment.equals(other.sentiment);
	}

	@Override
	public String toString() {
		return "Sentiment for " + sentence + ": " + sentiment;
	}
}