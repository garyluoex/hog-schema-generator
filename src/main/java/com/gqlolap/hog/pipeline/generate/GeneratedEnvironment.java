package com.gqlolap.hog.pipeline.generate;

import com.gqlolap.hog.pipeline.validate.ValidatedEnvironment;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * Created by weijialuo on 7/3/18.
 */
public class GeneratedEnvironment extends ValidatedEnvironment {

  private final ValidatedEnvironment sourceValidatedEnvironment;
  private final TypeDefinitionRegistry generatedTypeDefinitionRegistry;

  /**
   * TODO.
   *
   * @param validatedEnvironment TODO.
   * @param generatedTypeDefinitionRegistry TODO.
   */
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

  public GeneratedEnvironment withGeneratedTypeDeifnitionRegistry(
      TypeDefinitionRegistry newTypeDefinitionResgistry) {
    return new GeneratedEnvironment(sourceValidatedEnvironment, newTypeDefinitionResgistry);
  }
}
