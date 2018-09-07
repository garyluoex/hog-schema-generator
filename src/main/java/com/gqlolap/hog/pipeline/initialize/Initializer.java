package com.gqlolap.hog.pipeline.initialize;

import com.gqlolap.hog.Component;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.List;

/**
 * TODO.
 */
public interface Initializer extends Component {

  InitializedEnvironment initialize(TypeDefinitionRegistry sourceRegistry);
}
