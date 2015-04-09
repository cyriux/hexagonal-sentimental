package fr.devoxx.sentimental.infra;

import java.sql.Timestamp;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface AuditDAO {

	@SqlUpdate("create table TREND (id int primary key, timestamp timestamp, sentence varchar(140), sentiment varchar(24))")
	void createAuditTable();

	@SqlUpdate("insert into TREND (id, timestamp, sentence, sentiment) values (:id, :timestamp, :sentence, :sentiment)")
	long insert(@Bind("id") long id, @Bind("timestamp") Timestamp timestamp, @Bind("sentence") String sentence,
			@Bind("sentiment") String sentiment);

	@SqlQuery("select sentence from TREND where id = :id")
	String findSentenceById(@Bind("id") int id);

	@SqlQuery("select sentiment, count(*) from TREND group by sentiment")
	String findTrend();
}