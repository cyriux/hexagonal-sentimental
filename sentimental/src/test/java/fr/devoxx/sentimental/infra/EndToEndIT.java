package fr.devoxx.sentimental.infra;

import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Please start Postgres before running this end-to-end test")
public class EndToEndIT {

	@ClassRule
	public static final DropwizardAppRule<SentimentalConfiguration> RULE = new DropwizardAppRule<SentimentalConfiguration>(
			SentimentalApplication.class, ResourceHelpers.resourceFilePath("sentimental-test.yml"));

	@Test
	public void testGetSentimentAnalysis() {
		System.out.println(new Date().getTime());
		Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");

		// http://localhost:8080/sentiment-analysis/?sentence=Kitten+are+super+cool
		final String url = String.format("http://localhost:%d/sentiment-analysis", RULE.getLocalPort());
		final Builder request = client.target(url).queryParam("sentence", "Kitten are cool!").request();
		final Response response = request.get();
		
		assertThat(response.getStatus()).isEqualTo(200);
	}
}