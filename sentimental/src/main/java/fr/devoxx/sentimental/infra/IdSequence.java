package fr.devoxx.sentimental.infra;

import java.util.Date;

import fr.devoxx.sentimental.annotation.NotThereYet;

@NotThereYet
public enum IdSequence {

	INSTANCE;

	public long next() {
		// Dummy code, don't do that at home!
		return new Date().getTime() - 1428355255699l;
	}
}
