package com.gqlolap.hog;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.gqlolap.hog.adapter.InputAdapter;
import com.gqlolap.hog.pipeline.certify.CertifiedEnvironment;
import com.gqlolap.hog.pipeline.certify.Certifier;
import com.gqlolap.hog.pipeline.config.Configurator;

import com.gqlolap.hog.pipeline.config.ConfiguredEnvironment;
import com.gqlolap.hog.pipeline.generate.GeneratedEnvironment;
import com.gqlolap.hog.pipeline.generate.Generator;
import com.gqlolap.hog.pipeline.initialize.InitializedEnvironment;
import com.gqlolap.hog.pipeline.initialize.Initializer;
import com.gqlolap.hog.pipeline.validate.ValidatedEnvironment;
import com.gqlolap.hog.pipeline.validate.Validator;
import com.gqlolap.hog.serializer.SchemaSerializer;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * This Java source file was generated by the Gradle 'init' task.
 */
public class OlapSchemaGenerator {

  private final Injector injector;

  public OlapSchemaGenerator() {
    this(new DefaultBinderModule().useDefaultBindings());
  }

  public OlapSchemaGenerator(AbstractModule module) {
    this.injector = Guice.createInjector(module);
  }

  /**
   * TODO.
   *
   * @param inputAdapter TODO.
   * @return TODO.
   */
  public TypeDefinitionRegistry buildTypeDefinitionRegistry(InputAdapter inputAdapter) {

    InitializedEnvironment initializedEnvironment = initialize(
        inputAdapter.getSourceTypeDefinitionRegistry());

    ConfiguredEnvironment configuredEnvironment = configure(initializedEnvironment);

    ValidatedEnvironment validatedEnvironment = validate(configuredEnvironment);

    GeneratedEnvironment generatedEnvironment = generate(validatedEnvironment);

    CertifiedEnvironment certifiedEnvironment = certify(generatedEnvironment);

    return certifiedEnvironment.getGeneratedTypeDefinitionRegistry();
  }

  /**
   * TODO.
   *
   * @param inputAdapter TODO.
   * @return TODO.
   */
  public String buildTypeDefinitionString(InputAdapter inputAdapter) {
    return buildTypeDefinitionString(inputAdapter, injector.getInstance(SchemaSerializer.class));
  }

  /**
   * TODO.
   *
   * @param inputAdapter TODO.
   * @param schemaSerializer TODO.
   * @return TODO.
   */
  public String buildTypeDefinitionString(InputAdapter inputAdapter,
      SchemaSerializer schemaSerializer) {
    return schemaSerializer.serialize(buildTypeDefinitionRegistry(inputAdapter));
  }

  /**
   * TODO.
   *
   * @param sourceRegistry TODO.
   * @return TODO.
   */
  protected InitializedEnvironment initialize(TypeDefinitionRegistry sourceRegistry) {
    Initializer initializer = injector.getInstance(Initializer.class);
    return initializer.initialize(sourceRegistry);
  }

  /**
   * TODO.
   *
   * @param initializedEnvironment TODO.
   * @return TODO.
   */
  protected ConfiguredEnvironment configure(InitializedEnvironment initializedEnvironment) {
    Configurator configurator = injector.getInstance(Configurator.class);
    return configurator.configure(initializedEnvironment);
  }

  /**
   * TODO.
   *
   * @param configuredEnvironment TODO.
   * @return TODO.
   */
  protected ValidatedEnvironment validate(ConfiguredEnvironment configuredEnvironment) {
    Validator validator = injector.getInstance(Validator.class);
    return validator.validate(configuredEnvironment);
  }

  /**
   * TODO.
   *
   * @param validatedEnvironment TODO.
   * @return TODO.
   */
  protected GeneratedEnvironment generate(ValidatedEnvironment validatedEnvironment) {
    Generator generator = injector.getInstance(Generator.class);
    return generator.generate(validatedEnvironment);
  }

  /**
   * TODO.
   *
   * @param generatedEnvironment TODO.
   * @return TODO.
   */
  protected CertifiedEnvironment certify(GeneratedEnvironment generatedEnvironment) {
    Certifier certifier = injector.getInstance(Certifier.class);
    return certifier.certify(generatedEnvironment);
  }
}
