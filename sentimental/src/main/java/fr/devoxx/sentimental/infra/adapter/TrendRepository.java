package fr.devoxx.sentimental.infra.adapter;

import java.sql.Timestamp;

import fr.devoxx.sentimental.annotation.Repository;
import fr.devoxx.sentimental.domain.Sentiment;
import fr.devoxx.sentimental.domain.Trend;
import fr.devoxx.sentimental.infra.AuditDAO;
import fr.devoxx.sentimental.infra.IdSequence;

@Repository
public class TrendRepository implements Trend {

	private final AuditDAO auditDao;

	public TrendRepository(AuditDAO auditDao) {
		this.auditDao = auditDao;
	}

	public void record(String sentence, Sentiment sentiment) {
		final Timestamp timestamp = new Timestamp(now());
		auditDao.insert(IdSequence.INSTANCE.next(), timestamp, sentence, sentiment.name());
	}

	private long now() {
		return new java.util.Date().getTime();
	}

}
