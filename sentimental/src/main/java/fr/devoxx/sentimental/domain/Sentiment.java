package fr.devoxx.sentimental.domain;

public enum Sentiment {
	SAD() {

		@Override
		public Sentiment opposite() {
			return HAPPY;
		}

	},
	NEUTRAL, HAPPY() {

		@Override
		public Sentiment opposite() {
			return SAD;
		}

	};

	public Sentiment opposite() {
		return NEUTRAL;
	}
	
	// also WTF, LULZ, 
}
