package com.gqlolap.hog.pipeline.certify;

import com.gqlolap.hog.pipeline.PipelineStages;
import com.gqlolap.hog.pipeline.generate.GeneratedEnvironment;

/** Created by weijialuo on 7/3/18. */
public class CertifiedEnvironment extends GeneratedEnvironment {

  /**
   * TODO.
   *
   * @param generatedEnvironment TODO.
   */
  public CertifiedEnvironment(
      GeneratedEnvironment generatedEnvironment) {
    super(
        generatedEnvironment.getSourceValidatedEnvironment(),
        generatedEnvironment.getGeneratedTypeDefinitionRegistry());
  }

  @Override
  public PipelineStages getStage() {
    return PipelineStages.verify;
  }
}
