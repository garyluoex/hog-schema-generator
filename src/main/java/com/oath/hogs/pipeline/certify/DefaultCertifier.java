package com.oath.hogs.pipeline.certify;

import com.oath.hogs.pipeline.generate.GeneratedEnvironment;
import com.oath.hogs.processor.Processor;

/** Created by weijialuo on 7/2/18. */
public class DefaultCertifier implements Certifier {
  @Override
  public CertifiedEnvironment certify(final GeneratedEnvironment generatedEnvironment) {

    CertifiedEnvironment certifiedEnvironment = new CertifiedEnvironment(generatedEnvironment);

    for(Processor processor : certifiedEnvironment.getConfiguredProcessors().values()) {
      certifiedEnvironment = processor.certify(certifiedEnvironment);
    }

    return certifiedEnvironment;
  }
}
