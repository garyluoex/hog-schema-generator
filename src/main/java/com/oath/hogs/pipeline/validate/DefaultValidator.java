package com.oath.hogs.pipeline.validate;

import com.oath.hogs.pipeline.config.ConfiguredEnvironment;
import com.oath.hogs.processor.Processor;

/** Created by weijialuo on 7/2/18. */
public class DefaultValidator implements Validator {
  @Override
  public ValidatedEnvironment validate(final ConfiguredEnvironment configuredEnvironment) {

    ValidatedEnvironment validatedEnvironment = new ValidatedEnvironment(configuredEnvironment);

    for (Processor processor : configuredEnvironment.getConfiguredProcessors().values()) {
      validatedEnvironment = processor.validate(validatedEnvironment);
    }
    return validatedEnvironment;
  }
}
