package com.oath.hogs.pipeline.certify;

import com.oath.hogs.pipeline.generate.GeneratedEnvironment;

/** Created by weijialuo on 7/3/18. */
public interface Certifier {
  CertifiedEnvironment certify(GeneratedEnvironment generatedEnvironment);
}
