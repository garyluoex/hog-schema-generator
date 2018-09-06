package com.oath.hogs.pipeline.initialize;

import com.oath.hogs.pipeline.PipelineEnvironment;
import com.oath.hogs.pipeline.PipelineStages;
import graphql.schema.idl.TypeDefinitionRegistry;

/** Created by weijialuo on 7/3/18. */
public class InitializedEnvironment implements PipelineEnvironment {

  private final TypeDefinitionRegistry sourceDefinitionRegistry;
  private final TypeDefinitionRegistry hogsStaticDefinitionRegistry;

  public InitializedEnvironment(TypeDefinitionRegistry sourceDefinitionRegistry, TypeDefinitionRegistry staticDefinitionRegistry) {
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
