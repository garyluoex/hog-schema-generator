package com.gqlolap.hog.pipeline.config;

import com.gqlolap.hog.pipeline.PipelineStages;
import com.gqlolap.hog.pipeline.initialize.InitializedEnvironment;
import com.gqlolap.hog.processor.Processor;
import java.util.Map;

/**
 * Created by weijialuo on 7/3/18.
 */
public class ConfiguredEnvironment extends InitializedEnvironment {

  private final InitializedEnvironment sourceInitializedEnvironment;
  private final Map<String, Processor> configuredProcessors;

  /**
   * TODO.
   *
   * @param sourceInitializedEnvironment TODO.
   * @param configuredProcessors TODO.
   */
  public ConfiguredEnvironment(
      InitializedEnvironment sourceInitializedEnvironment,
      Map<String, Processor> configuredProcessors) {
    super(
        sourceInitializedEnvironment.getSourceTypeDefinitionRegistry(),
        sourceInitializedEnvironment.getStaticTypeDefinitionRegistry());

    this.sourceInitializedEnvironment = sourceInitializedEnvironment;
    this.configuredProcessors = configuredProcessors;
  }

  public InitializedEnvironment getSourceInitializedEnvironment() {
    return sourceInitializedEnvironment;
  }

  /**
   * TODO.
   *
   * @return TODO.
   */
  public Map<String, Processor> getConfiguredProcessors() {
    return configuredProcessors;
  }

  @Override
  public PipelineStages getStage() {
    return PipelineStages.configure;
  }
}
