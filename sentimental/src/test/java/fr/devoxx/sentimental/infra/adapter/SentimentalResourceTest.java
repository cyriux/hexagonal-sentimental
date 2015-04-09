package fr.devoxx.sentimental.infra.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.dropwizard.testing.junit.ResourceTestRule;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import fr.devoxx.sentimental.domain.Sentiment;
import fr.devoxx.sentimental.domain.SentimentAnalysis;

public class SentimentalResourceTest {

	private static final SentimentAnalysis analyzer = mock(SentimentAnalysis.class);

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
			.addResource(new SentimentalResource(analyzer)).build();

	private final SentimentReport report = new SentimentReport("Kitten", "HAPPY");

	@Before
	public void setup() {
		when(analyzer.sentimentOf(eq("Kitten"))).thenReturn(Sentiment.HAPPY);
		// we have to reset the mock after each test because of the
		// @ClassRule, or use a @Rule as mentioned below.
		//reset(analyzer);
	}

	@Test
	public void testSentimentAnalysis() {
		assertThat(resources.client().target("/sentiment-analysis?sentence=Kitten").request().get(SentimentReport.class))
				.isEqualTo(report);
		verify(analyzer).sentimentOf("Kitten");
	}
}
