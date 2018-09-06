package com.oath.hogs.pipeline.validate;

import com.oath.hogs.pipeline.PipelineStages;
import com.oath.hogs.pipeline.config.ConfiguredEnvironment;

/** Created by weijialuo on 7/3/18. */
public class ValidatedEnvironment extends ConfiguredEnvironment {

  private final ConfiguredEnvironment sourceConfiguredEnvironment;

  public ValidatedEnvironment(ConfiguredEnvironment sourceConfiguredEnvironment) {
    super(
        sourceConfiguredEnvironment.getSourceInitializedEnvironment(),
        sourceConfiguredEnvironment.getConfiguredProcessors());
    this.sourceConfiguredEnvironment = sourceConfiguredEnvironment;
  }

  public ConfiguredEnvironment getSourceConfiguredEnvironment() {
    return sourceConfiguredEnvironment;
  }

  @Override
  public PipelineStages getStage() {
    return PipelineStages.validate;
  }
}
