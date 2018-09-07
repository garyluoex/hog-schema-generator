package com.gqlolap.hog.pipeline.certify;

import com.gqlolap.hog.Component;
import com.gqlolap.hog.pipeline.generate.GeneratedEnvironment;

/**
 * Created by weijialuo on 7/3/18.
 */
public interface Certifier extends Component {

  CertifiedEnvironment certify(GeneratedEnvironment generatedEnvironment);
}
