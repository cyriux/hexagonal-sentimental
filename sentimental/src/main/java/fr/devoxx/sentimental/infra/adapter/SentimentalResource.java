package fr.devoxx.sentimental.infra.adapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.codahale.metrics.annotation.Timed;

import fr.devoxx.sentimental.domain.Sentiment;
import fr.devoxx.sentimental.domain.SentimentAnalysis;

@Path("/sentiment-analysis")
@Produces(MediaType.APPLICATION_JSON)
public class SentimentalResource {

	private final SentimentAnalysis analyzer;

	public SentimentalResource(SentimentAnalysis analyzer) {
		this.analyzer = analyzer;
	}

	@GET
	@Timed
	public SentimentReport saySentiment(@QueryParam("sentence") Optional<String> sentence) {
		final String safeSentence = sentence.or("");
		final Sentiment sentiment = analyzer.sentimentOf(safeSentence);
		return new SentimentReport(safeSentence, sentiment.toString());
	}
}