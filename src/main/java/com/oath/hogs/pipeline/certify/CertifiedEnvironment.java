package com.oath.hogs.pipeline.certify;

import com.oath.hogs.pipeline.PipelineStages;
import com.oath.hogs.pipeline.generate.GeneratedEnvironment;

/** Created by weijialuo on 7/3/18. */
public class CertifiedEnvironment extends GeneratedEnvironment {



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
