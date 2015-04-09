package fr.devoxx.sentimental.infra.adapter;

import java.io.File;

import twitter4j.Status;

import com.twitter.hbc.twitter4j.handler.StatusStreamHandler;

import fr.devoxx.sentimental.domain.Sentiment;
import fr.devoxx.sentimental.domain.SentimentAnalysis;
import fr.devoxx.sentimental.infra.TwitterStream;
import fr.devoxx.sentimental.infra.TwitterStream.DefaultStatusStreamHandler;
import fr.devoxx.sentimental.infra.audio.SamplePlayer;

public class TwitterAdapter {

	private final SamplePlayer samplePlayer;
	private final SentimentAnalysis service;
	private final TwitterStream twitterStream = new TwitterStream();

	public TwitterAdapter(final SentimentAnalysis service, SamplePlayer samplePlayer) {
		this.service = service;
		this.samplePlayer = samplePlayer;
	}

	public void subscribe(final String... terms) {
		final StatusStreamHandler listener = new DefaultStatusStreamHandler() {

			public void onStatus(Status status) {
				final Sentiment sentiment = service.sentimentOf(status.getText());
				System.out.println("*********************");
				System.out.println(status.getText() + " => " + sentiment);

				final String sampleName = sentiment.toString().toLowerCase();
				final File file = new File(sampleName + ".wav");
				samplePlayer.play(file);
			}
		};

		twitterStream.subscribe(listener, terms);
	}

}
