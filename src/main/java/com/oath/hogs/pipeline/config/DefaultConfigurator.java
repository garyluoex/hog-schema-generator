package com.oath.hogs.pipeline.config;

import com.google.inject.Inject;
import com.oath.hogs.pipeline.initialize.InitializedEnvironment;
import com.oath.hogs.processor.Processor;
import java.util.Map;

/** Created by weijialuo on 7/2/18. */
public class DefaultConfigurator implements Configurator {

  private final Map<String, Processor> configuredProcessors;

  @Inject
  public DefaultConfigurator(Map<String, Processor> configuredProcessors) {
    this.configuredProcessors = configuredProcessors;
  }

  @Override
  public ConfiguredEnvironment configure(final InitializedEnvironment initializedEnvironment) {

    ConfiguredEnvironment configuredEnvironment = new ConfiguredEnvironment(initializedEnvironment, configuredProcessors);

    for (Processor processor : configuredProcessors.values()) {
      configuredEnvironment = processor.configure(configuredEnvironment);
    }

    return configuredEnvironment;
  }
}
