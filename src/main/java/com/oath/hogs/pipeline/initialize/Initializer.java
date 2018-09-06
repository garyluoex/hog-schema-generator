package com.oath.hogs.pipeline.initialize;

import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.List;

/** Created by weijialuo on 7/3/18. */
public interface Initializer {

  InitializedEnvironment initialize(TypeDefinitionRegistry sourceRegistry);
}
