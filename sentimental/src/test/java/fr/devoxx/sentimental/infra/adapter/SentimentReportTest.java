package fr.devoxx.sentimental.infra.adapter;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import fr.devoxx.sentimental.infra.adapter.SentimentReport;
import io.dropwizard.jackson.Jackson;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SentimentReportTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void serializesToJSON() throws Exception {
		final SentimentReport person = new SentimentReport("Hello Kitten", "HAPPY");
		assertThat(MAPPER.writeValueAsString(person)).isEqualTo(fixture("fixtures/sentiment-report.json"));
	}
}
