package com.mttnow.acceptancetests;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.steps.InjectableStepsFactory;

public abstract class AbstractAcceptanceTestStories extends JUnitStories {

	private static final String DEFAULT_INCLUDE_PATTERN = "**/*.story";
	private final AbstractAcceptanceTestEmbedder embedder;
	private final String includePattern;
	private final String storyDir;

	public AbstractAcceptanceTestStories(AbstractAcceptanceTestEmbedder embedder) {
		this.embedder = embedder;
		String includePattern = System.getProperty("acceptance.tests.include.pattern");
		if (StringUtils.isNotBlank(includePattern)) {
			this.includePattern = includePattern;
		} else {
			this.includePattern = DEFAULT_INCLUDE_PATTERN;
		}
		storyDir = System.getProperty("acceptance.tests.story.dir");
	}

	public AbstractAcceptanceTestStories(AbstractAcceptanceTestEmbedder embedder, int storyTimeOutInSeconds) {
		this(embedder);
		useStoryTimeoutInSecs(storyTimeOutInSeconds);
	}

	@Override
	public Configuration configuration() {
		return this.embedder.configuration();
	}

	/**
	 * Jbehave have a default timeout of 300s for running stories
	 * When using selenium acceptance tests, this may take longer if running all the
	 * tests in the build. This is because the pages have to be loaded into the junit
	 * webDriver.....use this to increase the timout if needed
	 *
	 * @param seconds to increase the running of stories timeout
	 */
	public void useStoryTimeoutInSecs(int seconds) {
		configuredEmbedder().embedderControls().useThreads(1).useStoryTimeoutInSecs(seconds);
		this.embedder.useEmbedderControls(this.embedder.embedderControls().useThreads(1).useStoryTimeoutInSecs(seconds));
	}

	@Override
	public InjectableStepsFactory stepsFactory() {
		return this.embedder.stepsFactory();
	}

	@Override
	protected List<String> storyPaths() {
		if (StringUtils.isNotBlank(storyDir))
			return new StoryFinder().findPaths(storyDir, Arrays.asList(includePattern), Collections.<String>emptyList());
		else
			return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), includePattern, "");
	}
}
