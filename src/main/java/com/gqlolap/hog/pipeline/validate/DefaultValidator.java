package com.gqlolap.hog.pipeline.validate;

import com.gqlolap.hog.pipeline.config.ConfiguredEnvironment;
import com.gqlolap.hog.processor.Processor;

/**
 * TODO.
 */
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
