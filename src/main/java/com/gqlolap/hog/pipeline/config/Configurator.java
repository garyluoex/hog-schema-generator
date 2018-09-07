package com.gqlolap.hog.pipeline.config;

import com.gqlolap.hog.Component;
import com.gqlolap.hog.pipeline.initialize.InitializedEnvironment;

/**
 * Created by weijialuo on 7/3/18.
 */
public interface Configurator extends Component {

  ConfiguredEnvironment configure(InitializedEnvironment initializedEnvironment);
}
