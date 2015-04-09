package fr.devoxx.sentimental.infra;

import fr.devoxx.sentimental.annotation.NotThereYet;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

@NotThereYet
public class SentimentalConfiguration extends Configuration {

	@NotEmpty
	@JsonProperty
	
	private String template;
	@NotEmpty
	@JsonProperty
	private String lexiconFileName = "lexicon.properties";

	@Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
    
    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
    this.database = dataSourceFactory;
    }
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getLexiconFileName() {
		return lexiconFileName;
	}

	public void setLexiconFileName(String lexiconFileName) {
		this.lexiconFileName = lexiconFileName;
	}
	
	

}
