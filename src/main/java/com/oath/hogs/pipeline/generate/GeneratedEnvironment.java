package com.oath.hogs.pipeline.generate;

import graphql.schema.idl.TypeDefinitionRegistry;
import com.oath.hogs.pipeline.validate.ValidatedEnvironment;

/** Created by weijialuo on 7/3/18. */
public class GeneratedEnvironment extends ValidatedEnvironment {

  private final ValidatedEnvironment sourceValidatedEnvironment;
  private final TypeDefinitionRegistry generatedTypeDefinitionRegistry;


  public GeneratedEnvironment(
      ValidatedEnvironment validatedEnvironment,
      TypeDefinitionRegistry generatedTypeDefinitionRegistry) {

    super(validatedEnvironment.getSourceConfiguredEnvironment());

    this.sourceValidatedEnvironment = validatedEnvironment;

    this.generatedTypeDefinitionRegistry = generatedTypeDefinitionRegistry;
  }

  public ValidatedEnvironment getSourceValidatedEnvironment() {
    return sourceValidatedEnvironment;
  }

  public TypeDefinitionRegistry getGeneratedTypeDefinitionRegistry() {
    return generatedTypeDefinitionRegistry;
  }

  public GeneratedEnvironment withGeneratedTypeDeifnitionRegistry(TypeDefinitionRegistry newTypeDefinitionResgistry) {
    return new GeneratedEnvironment(sourceValidatedEnvironment, newTypeDefinitionResgistry);
  }
}
