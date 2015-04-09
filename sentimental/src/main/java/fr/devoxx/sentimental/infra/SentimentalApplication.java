package fr.devoxx.sentimental.infra;

import fr.devoxx.sentimental.domain.Lexicon;
import fr.devoxx.sentimental.domain.SentimentAnalysis;
import fr.devoxx.sentimental.domain.Trend;
import fr.devoxx.sentimental.infra.adapter.FileBasedLexicon;
import fr.devoxx.sentimental.infra.adapter.SentimentalResource;
import fr.devoxx.sentimental.infra.adapter.TrendRepository;
import fr.devoxx.sentimental.infra.adapter.TwitterAdapter;
import fr.devoxx.sentimental.infra.audio.SamplePlayer;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.skife.jdbi.v2.DBI;

public class SentimentalApplication extends Application<SentimentalConfiguration> {

	public static void main(String[] args) throws Exception {
		new SentimentalApplication().run(args);
	}

	@Override
	public String getName() {
		return "sentimental";
	}

	@Override
	public void initialize(Bootstrap<SentimentalConfiguration> bootstrap) {
		// nothing to do yet
	}

	@Override
	public void run(SentimentalConfiguration configuration, Environment environment) {
		final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
		environment.healthChecks().register("template", healthCheck);

		// SPI Lexicon
		final Lexicon lexicon = new FileBasedLexicon(configuration.getLexiconFileName());

		// SPI Trend
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
		final AuditDAO auditDao = jdbi.onDemand(AuditDAO.class);
		final Trend audit = new TrendRepository(auditDao);

		// Domain Model
		final SentimentAnalysis service = new SentimentAnalysis(lexicon, audit);

		// API RESTful
		final SentimentalResource resource = new SentimentalResource(service);
		environment.jersey().register(resource);

		// API Twitter
		final TwitterAdapter twitterAdapter = new TwitterAdapter(service, new SamplePlayer());
		twitterAdapter.subscribe("devoxx, #memepasmal, @cyriux, @tpierrain, arolla");
		// start
	}

}