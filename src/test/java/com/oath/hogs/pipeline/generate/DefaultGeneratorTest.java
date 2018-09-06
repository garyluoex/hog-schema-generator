package com.oath.hogs.pipeline.generate;

import com.oath.hogs.TestingHelperMethods;
import com.oath.hogs.adapter.GraphqlInputAdapter;
import com.oath.hogs.pipeline.config.ConfiguredEnvironment;
import com.oath.hogs.pipeline.initialize.DefaultInitializer;
import com.oath.hogs.pipeline.initialize.InitializedEnvironment;
import com.oath.hogs.pipeline.validate.ValidatedEnvironment;
import com.oath.hogs.processor.Processor;
import com.oath.hogs.processor.StaticProcessor;
import com.oath.hogs.processor.where.WhereOperationProcessor;
import com.oath.hogs.serializer.DefaultGraphqlSerializer;
import graphql.language.Definition;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class DefaultGeneratorTest {

  private static String testGraphqlSchemaLocation = "src/test/resources/test.graphqls";
  private File schemaFile;
  private DefaultGraphqlSerializer serializer;
  private InitializedEnvironment initializedEnvironment;
  private DefaultInitializer initializer;
  private ConfiguredEnvironment configuredEnvironment;
  private ValidatedEnvironment validatedEnvironment;

  @Before
  public void setUp() throws Exception {
    this.schemaFile = new File(testGraphqlSchemaLocation);
    this.serializer = new DefaultGraphqlSerializer();
    this.initializer = new DefaultInitializer();
    GraphqlInputAdapter inputAdapter = new GraphqlInputAdapter(schemaFile);
    this.initializedEnvironment = initializer.initialize(inputAdapter.getSourceTypeDefinitionRegistry());
    Map<String, Processor> configuredProcessors = new HashMap<>();
    configuredProcessors.put("WhereOperationProcessor", new WhereOperationProcessor());
    configuredProcessors.put("StaticProcessor", new StaticProcessor());
    this.configuredEnvironment = new ConfiguredEnvironment(initializedEnvironment, configuredProcessors);
    this.validatedEnvironment = new ValidatedEnvironment(configuredEnvironment);
  }

  @Test
  public void testGenerate() {

    DefaultGenerator defaultGenerator = new DefaultGenerator();

    TypeDefinitionRegistry resultTypeDefinitionRegistry =
        defaultGenerator.generate(validatedEnvironment).getGeneratedTypeDefinitionRegistry();

    List<Definition> allTypeDefs = TestingHelperMethods.getAllDefinitions(resultTypeDefinitionRegistry);

    allTypeDefs.stream().map(serializer::serialize).forEach(System.out::println);
  }
}
