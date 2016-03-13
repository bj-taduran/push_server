package com.mttnow.acceptancetests;

import com.mttnow.acceptancetests.steps.AcceptanceSteps;
import com.mttnow.acceptancetests.steps.BaseRestAcceptanceSteps;
import com.mttnow.acceptancetests.steps.BaseWebAcceptanceSteps;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryLocation;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.reflections.Reflections;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.XML;

public class AbstractAcceptanceTestEmbedder extends Embedder {

  public static final String COMMON_STEPS_PACKAGE = "com.mttnow.acceptancetests.steps";
  private String[] stepsPackages;
  private Configuration configuration;

  private static final int NUMBER_OF_THREADS = 1;
  private int timeoutSeconds = 300;

  public AbstractAcceptanceTestEmbedder(String... stepsPackages) {
    this.stepsPackages = stepsPackages;
    buildConfiguration();
  }

  public AbstractAcceptanceTestEmbedder(String[] stepsPackages, int storyTimoutInSeconds) {
    this(stepsPackages);
    timeoutSeconds = storyTimoutInSeconds;
  }

  public void setStoryTimoutSeconds(int seconds) {
    timeoutSeconds = seconds;
  }

  @Override
  public EmbedderControls embedderControls() {
    return new EmbedderControls().doGenerateViewAfterStories(true).doIgnoreFailureInStories(false).doIgnoreFailureInView(false).useThreads(NUMBER_OF_THREADS).useStoryTimeoutInSecs(timeoutSeconds);
  }

  @Override
  public Configuration configuration() {
    return configuration;
  }

  @Override
  public InjectableStepsFactory stepsFactory() {
    Reflections commonSteps = new Reflections(COMMON_STEPS_PACKAGE);
    Reflections projectSteps = new Reflections((Object[])stepsPackages);

    Set<Class<? extends AcceptanceSteps>> acceptanceStepsClasses = commonSteps.getSubTypesOf(AcceptanceSteps.class);
    acceptanceStepsClasses.addAll(commonSteps.getSubTypesOf(BaseWebAcceptanceSteps.class));
    acceptanceStepsClasses.addAll(projectSteps.getSubTypesOf(BaseRestAcceptanceSteps.class));
    
    Set<AcceptanceSteps> steps = new HashSet<AcceptanceSteps>();
    for (Class<? extends AcceptanceSteps> clazz : acceptanceStepsClasses) {
      AcceptanceSteps step = newInstance(clazz);
      steps.add(step);
    }

    return new InstanceStepsFactory(configuration, steps.toArray());
  }

  private Configuration buildConfiguration() {
    Class<? extends AbstractAcceptanceTestEmbedder> embeddableClass = this.getClass();
    configuration = new MostUsefulConfiguration();
    configuration.useStoryLoader(new LoadFromClasspath(embeddableClass));
    configuration.useStoryReporterBuilder(createReporterBuilder(embeddableClass));
    return configuration;
  }

  private StoryReporterBuilder createReporterBuilder(Class<? extends AbstractAcceptanceTestEmbedder> embeddableClass) {
    StoryReporterBuilder storyReporterBuilder = new AcceptanceTestStoryReporterBuilder().withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass)).withDefaultFormats().withFormats(CONSOLE, XML).withFailureTrace(true);
    deleteOldReports(storyReporterBuilder);
    return storyReporterBuilder;
  }

  private void deleteOldReports(StoryReporterBuilder storyReporterBuilder) {
    if (FileUtils.deleteQuietly(storyReporterBuilder.outputDirectory())) {
      System.out.println("Story report folder deleted: " + storyReporterBuilder.outputDirectory().getPath());
    }
  }

  private AcceptanceSteps newInstance(Class<? extends AcceptanceSteps> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      //ignore
    }
    return null;
  }


  private static final class AcceptanceTestStoryReporterBuilder extends StoryReporterBuilder {

    @Override
    protected FilePrintStreamFactory filePrintStreamFactory(String storyPath) {
      return new AcceptanceTestFilePrintStreamFactory(new StoryLocation(super.codeLocation(), storyPath), fileConfiguration(""));
    }

  }

  private static final class AcceptanceTestFilePrintStreamFactory extends FilePrintStreamFactory {

    public static final String REPORTER_OUTPUT_DIRECTORY = "acceptance.tests.reporter.output.directory";

    public AcceptanceTestFilePrintStreamFactory(StoryLocation storyLocation, FileConfiguration configuration) {
      super(storyLocation, configuration);
    }

    @Override
    protected File outputDirectory() {
      String viewDirectory = System.getProperty(REPORTER_OUTPUT_DIRECTORY);
      if (StringUtils.isNotBlank(viewDirectory)) {
        return new File(viewDirectory);
      }

      return super.outputDirectory();
    }
  }
}
