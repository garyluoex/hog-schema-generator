package com.oath.hogs.pipeline.config;

import com.oath.hogs.pipeline.initialize.InitializedEnvironment;

/** Created by weijialuo on 7/3/18. */
public interface Configurator {
  ConfiguredEnvironment configure(InitializedEnvironment initializedEnvironment);
}
