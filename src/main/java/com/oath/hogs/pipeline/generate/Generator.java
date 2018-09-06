package com.oath.hogs.pipeline.generate;

import com.oath.hogs.pipeline.validate.ValidatedEnvironment;

/** Created by weijialuo on 7/3/18. */
public interface Generator {
  GeneratedEnvironment generate(ValidatedEnvironment validatedEnvironment);
}
