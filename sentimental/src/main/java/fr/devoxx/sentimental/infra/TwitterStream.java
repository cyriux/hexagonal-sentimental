package fr.devoxx.sentimental.infra;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jStatusClient;
import com.twitter.hbc.twitter4j.handler.StatusStreamHandler;
import com.twitter.hbc.twitter4j.message.DisconnectMessage;
import com.twitter.hbc.twitter4j.message.StallWarningMessage;

import fr.devoxx.sentimental.annotation.NotThereYet;

@NotThereYet
public class TwitterStream {

	public static class DefaultStatusStreamHandler implements StatusStreamHandler {

		public void onStatus(Status arg0) {
		}

		public void onException(Exception arg0) {
		}

		public void onTrackLimitationNotice(int arg0) {
		}

		public void onStallWarning(StallWarning arg0) {
		}

		public void onScrubGeo(long arg0, long arg1) {
		}

		public void onDeletionNotice(StatusDeletionNotice arg0) {
		}

		public void onUnknownMessageType(String arg0) {
		}

		public void onStallWarningMessage(StallWarningMessage arg0) {
		}

		public void onDisconnectMessage(DisconnectMessage arg0) {
		}
	}

	public static void main(String[] args) {

		final StatusStreamHandler listener = new DefaultStatusStreamHandler() {

			public void onStatus(Status status) {
				System.out.println("*********************");
				System.out.println(status.getText() + " => " + "?");
			}

		};
		new TwitterStream().subscribe(listener, "#masterchef");
	}

	public void subscribe(final StatusStreamHandler listener, String... terms) {
		/**
		 * Set up your blocking queues: Be sure to size these properly based on
		 * expected TPS of your stream
		 */
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

		/**
		 * Declare the host you want to connect to, the endpoint, and
		 * authentication (basic auth or oauth)
		 */
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		hosebirdEndpoint.trackTerms(Lists.newArrayList(terms));

		Authentication hosebirdAuth = oAuth();

		ClientBuilder builder = new ClientBuilder().name("Hosebird-Client-01")
				// optional: mainly for the logs
				.hosts(hosebirdHosts).authentication(hosebirdAuth).endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue)).eventMessageQueue(eventQueue);
		Client client = builder.build();

		final ExecutorService executorService = Executors.newFixedThreadPool(1);
		final Twitter4jStatusClient t4jClient = new Twitter4jStatusClient(client, msgQueue,
				Lists.newArrayList(listener), executorService);
		t4jClient.connect();

		// Call this once for every thread you want to spin off for processing
		// the raw messages.
		// This should be called at least once.
		t4jClient.process(); // required to start processing the messages
	}

	private static Authentication oAuth() {
		final Properties properties = new Properties();
		final String fileName = "config.properties";
		final InputStream is = TwitterStream.class.getClassLoader().getResourceAsStream(fileName);
		if (is != null) {
			try {
				properties.load(is);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} else {
			System.err.println("property file '" + fileName + "' not found in the classpath");
		}
		final String consumerKey = properties.getProperty("consumerKey");
		final String consumerSecret = properties.getProperty("consumerSecret");
		final String accessToken = properties.getProperty("accessToken");
		final String accessTokenSecret = properties.getProperty("accessTokenSecret");

		Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		return hosebirdAuth;
	}
}
