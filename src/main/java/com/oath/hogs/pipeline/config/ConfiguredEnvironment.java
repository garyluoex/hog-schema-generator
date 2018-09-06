package com.oath.hogs.pipeline.config;

import com.oath.hogs.pipeline.PipelineStages;
import com.oath.hogs.pipeline.initialize.InitializedEnvironment;
import com.oath.hogs.processor.Processor;
import java.util.List;
import java.util.Map;

/** Created by weijialuo on 7/3/18. */
public class ConfiguredEnvironment extends InitializedEnvironment {

  private final InitializedEnvironment sourceInitializedEnvironment;
  private final Map<String, Processor> configuredProcessors;

  public ConfiguredEnvironment(
      InitializedEnvironment sourceInitializedEnvironment, Map<String, Processor> configuredProcessors) {
    super(
        sourceInitializedEnvironment.getSourceTypeDefinitionRegistry(),
        sourceInitializedEnvironment.getStaticTypeDefinitionRegistry());

    this.sourceInitializedEnvironment = sourceInitializedEnvironment;
    this.configuredProcessors = configuredProcessors;
  }

  public InitializedEnvironment getSourceInitializedEnvironment() {
    return sourceInitializedEnvironment;
  }

  public Map<String, Processor> getConfiguredProcessors() {
    return configuredProcessors;
  }

  @Override
  public PipelineStages getStage() {
    return PipelineStages.configure;
  }
}
