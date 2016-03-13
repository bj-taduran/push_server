package com.mttnow.acceptancetests.steps;

import static java.lang.System.getProperty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeStories;

import com.mttnow.acceptancetests.utils.JettyStarter;
import com.mttnow.acceptancetests.utils.NetworkPortSelector;
import com.mttnow.acceptancetests.utils.UnreachablePortRangeException;

public final class ConfigurationSteps extends AcceptanceSteps {

	private static final int DEFAULT_JETTY_PORT_RANGE_FROM = 18080;
	private static final int DEFAULT_JETTY_PORT_RANGE_TO = 18180;
	private static final String PROPERTY_LOCAL_MODE = "acceptance.tests.local.mode";
	private static final String PROPERTY_WAR_DIR = "acceptance.tests.war.dir";
	private static final String PROPERTY_CONTEXT = "acceptance.tests.context";

	private final boolean localMode;
	private String warDir;

	public ConfigurationSteps() {
		localMode = isLocalMode();
		if (localMode) {
			warDir = getProperty(PROPERTY_WAR_DIR);
			assertNotNull(PROPERTY_WAR_DIR, warDir);
		}
	}

	public static boolean isLocalMode() {
		String sysPropLocalMode = getProperty(PROPERTY_LOCAL_MODE);
		if (StringUtils.isNotBlank(sysPropLocalMode)) {
			return Boolean.valueOf(sysPropLocalMode);
		} else {
			return true;
		}
	}

	@BeforeStories
	public void setUp() throws Exception {
		logDebug("Setting up acceptance test environment");
		if (localMode) {
			int jettyPort = getJettyPort();
			String context = getProperty(PROPERTY_CONTEXT);
			assertNotNull("The context must be set", context);
			JettyStarter.start(jettyPort, context, this.warDir);
			assertTrue(JettyStarter.isStarted());
			String endpoint = "http://localhost:" + jettyPort + "/" + context + "/";
			System.setProperty("api.client.service.url", endpoint);
			logDebug("Client endpoint: " + endpoint);
		}
	}

	@AfterStories
	public void tearDown() throws Exception {
		logDebug("Cleaning up acceptance test environment");
		if (localMode) {
			assertTrue(JettyStarter.stop());
		}
	}

	private int getJettyPort() throws UnreachablePortRangeException {
		return NetworkPortSelector.fromRange(DEFAULT_JETTY_PORT_RANGE_FROM, DEFAULT_JETTY_PORT_RANGE_TO);
	}

}
