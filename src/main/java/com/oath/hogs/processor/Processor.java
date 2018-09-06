package com.oath.hogs.processor;

import com.oath.hogs.pipeline.certify.CertifiedEnvironment;
import com.oath.hogs.pipeline.config.ConfiguredEnvironment;
import com.oath.hogs.pipeline.generate.GeneratedEnvironment;
import com.oath.hogs.pipeline.initialize.InitializedEnvironment;
import com.oath.hogs.pipeline.validate.ValidatedEnvironment;

public interface Processor {

  default ConfiguredEnvironment configure(ConfiguredEnvironment configuredEnvironment) {
    return configuredEnvironment;
  }

  default ValidatedEnvironment validate(ValidatedEnvironment validatedEnvironment) {
    return validatedEnvironment;
  }

  default GeneratedEnvironment generate(GeneratedEnvironment generatedEnvironment) {
    return generatedEnvironment;
  }

  default CertifiedEnvironment certify(CertifiedEnvironment certifiedEnvironment) {
    return certifiedEnvironment;
  }
}
