package com.gqlolap.hog.processor;

import com.gqlolap.hog.pipeline.certify.CertifiedEnvironment;
import com.gqlolap.hog.pipeline.config.ConfiguredEnvironment;
import com.gqlolap.hog.pipeline.generate.GeneratedEnvironment;
import com.gqlolap.hog.pipeline.validate.ValidatedEnvironment;


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
