package com.gqlolap.hog.pipeline.generate;

import com.gqlolap.hog.Component;
import com.gqlolap.hog.pipeline.validate.ValidatedEnvironment;

/**
 * TODO.
 */
public interface Generator extends Component {

  GeneratedEnvironment generate(ValidatedEnvironment validatedEnvironment);
}
