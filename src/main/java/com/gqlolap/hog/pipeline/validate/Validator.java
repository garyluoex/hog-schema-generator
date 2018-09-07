package com.gqlolap.hog.pipeline.validate;

import com.gqlolap.hog.Component;
import com.gqlolap.hog.pipeline.config.ConfiguredEnvironment;

/**
 * TODO.
 */
public interface Validator extends Component {

  ValidatedEnvironment validate(ConfiguredEnvironment configuredEnvironment);
}
