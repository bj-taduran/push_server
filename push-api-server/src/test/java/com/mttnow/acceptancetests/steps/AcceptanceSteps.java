package com.mttnow.acceptancetests.steps;

import static com.mttnow.acceptancetests.StoryContext.getFromContext;
import static com.mttnow.acceptancetests.StoryContext.putInContext;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AcceptanceSteps {

	private static final Logger logger = LoggerFactory.getLogger(AcceptanceSteps.class);

	protected static final String CLIENT_VERSION = "clientVersion";
	protected static final String CURRENT_LOCALE = "currentLocale";
	protected static final String SESSION_HANDLER = "sessionHandler";

	protected void logDebug(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

	protected void logInfo(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}

	public static void setCurrentLocale(Locale locale) {
		putInContext(CURRENT_LOCALE, locale);
	}

	protected static String getCampaign() {
		String campaign = System.getProperty("acceptance.tests.campaign");
		return isBlank(campaign) ? campaign : "mock";
	}

	protected static Locale getCurrentLocale() {
		return getFromContext(CURRENT_LOCALE);
	}

	protected boolean isParamSet(String param) {
		return param != null && !"null".equals(param);
	}

}
