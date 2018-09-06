package com.oath.hogs.adapter.yaml;

import com.oath.hogs.TestingHelperMethods;
import com.oath.hogs.pipeline.config.ConfiguredEnvironment;
import com.oath.hogs.pipeline.generate.DefaultGenerator;
import com.oath.hogs.pipeline.generate.GeneratedEnvironment;
import com.oath.hogs.pipeline.initialize.DefaultInitializer;
import com.oath.hogs.pipeline.initialize.InitializedEnvironment;
import com.oath.hogs.pipeline.validate.ValidatedEnvironment;
import com.oath.hogs.processor.Processor;
import com.oath.hogs.processor.StaticProcessor;
import com.oath.hogs.processor.where.WhereOperationProcessor;
import com.oath.hogs.serializer.DefaultGraphqlSerializer;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class YamlInputAdapterTest {

  private List<String> yamlFileLocations =
      Arrays.asList(
          "src/test/resources/platform.yaml",
          "src/test/resources/platformLine.yaml",
          "src/test/resources/platformOrder.yaml",
          "src/test/resources/platformAccount.yaml");

  private List<File> datasourceFile = Collections.singletonList(new File("src/test/resources/datasources.yaml"));
  private List<File> metricFile = Collections.singletonList(new File("src/test/resources/metrics.yaml"));
  private List<File> dimensionFiles =  yamlFileLocations.stream().map(File::new).collect(Collectors.toList());
  private YamlInputAdapter yamlAdapter;
  private DefaultGraphqlSerializer serializer;
  private InitializedEnvironment initializedEnvironment;
  private DefaultInitializer initializer;

  @Before
  public void setUp() throws Exception {
    this.dimensionFiles = yamlFileLocations.stream().map(File::new).collect(Collectors.toList());
    this.initializer = new DefaultInitializer();
    this.initializedEnvironment = initializer.initialize(new TypeDefinitionRegistry());
    this.serializer = new DefaultGraphqlSerializer();
  }

  @Test
  public void testParseSourceFiles() {

    this.yamlAdapter = new YamlInputAdapter(datasourceFile, dimensionFiles, metricFile);

    TypeDefinitionRegistry typeDefinitionRegistry = yamlAdapter.getSourceTypeDefinitionRegistry();

    InitializedEnvironment environment =
        new InitializedEnvironment(
            typeDefinitionRegistry, initializedEnvironment.getStaticTypeDefinitionRegistry());

    DefaultGenerator generator = new DefaultGenerator();

    Map<String, Processor> configuredProcessors = new HashMap<>();
    configuredProcessors.put("WhereOperationProcessor", new WhereOperationProcessor());
    configuredProcessors.put("StaticProcessor", new StaticProcessor());

    GeneratedEnvironment generatedEnvironment =
        generator.generate(new ValidatedEnvironment(new ConfiguredEnvironment(environment, configuredProcessors)));

    TestingHelperMethods.getAllDefinitions(
            generatedEnvironment.getGeneratedTypeDefinitionRegistry())
        .stream()
        .map(serializer::serialize)
        .forEach(System.out::println);
  }
}
