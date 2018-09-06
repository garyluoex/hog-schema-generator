package com.oath.hogs.pipeline.validate;

import com.oath.hogs.pipeline.config.ConfiguredEnvironment;

/** Created by weijialuo on 7/3/18. */
public interface Validator {

  ValidatedEnvironment validate(ConfiguredEnvironment configuredEnvironment);
}
