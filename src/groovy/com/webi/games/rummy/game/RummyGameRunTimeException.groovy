package com.webi.games.rummy.game;

/**
 * Exception class to be handled for appropriate error page display
 * 
 * @author Suman Jakkula
 */
public class RummyGameRunTimeException extends Exception {

	private static final long serialVersionUID = 8245974122333131997L;

	public RummyGameRunTimeException() {
		
	}

	public RummyGameRunTimeException(String arg0) {
		super(arg0);
	}

	public RummyGameRunTimeException(Throwable cause) {
		super(cause);
	}

	public RummyGameRunTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RummyGameRunTimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
