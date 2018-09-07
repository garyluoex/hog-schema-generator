package com.gqlolap.hog.pipeline.initialize;

import com.gqlolap.hog.pipeline.PipelineEnvironment;
import com.gqlolap.hog.pipeline.PipelineStages;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * TODO.
 */
public class InitializedEnvironment implements PipelineEnvironment {

  private final TypeDefinitionRegistry sourceDefinitionRegistry;
  private final TypeDefinitionRegistry hogsStaticDefinitionRegistry;

  public InitializedEnvironment(TypeDefinitionRegistry sourceDefinitionRegistry,
      TypeDefinitionRegistry staticDefinitionRegistry) {
    this.sourceDefinitionRegistry = sourceDefinitionRegistry;
    this.hogsStaticDefinitionRegistry = staticDefinitionRegistry;
  }

  public TypeDefinitionRegistry getStaticTypeDefinitionRegistry() {
    return hogsStaticDefinitionRegistry;
  }

  public TypeDefinitionRegistry getSourceTypeDefinitionRegistry() {
    return sourceDefinitionRegistry;
  }

  @Override
  public PipelineStages getStage() {
    return PipelineStages.initialize;
  }
}
