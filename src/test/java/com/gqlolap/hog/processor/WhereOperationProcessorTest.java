package com.gqlolap.hog.processor;

import com.gqlolap.hog.processor.where.WhereOperationProcessor;
import com.gqlolap.hog.serializer.DefaultGraphqlSerializer;
import graphql.language.InputObjectTypeDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class WhereOperationProcessorTest {

  private static String testGraphqlSchemaLocation = "src/test/resources/test.graphqls";
  private File schemaFile;
  private SchemaParser schemaParser;
  private DefaultGraphqlSerializer serializer;
  private TypeDefinitionRegistry expectedTypeDefinitionRegistry;
  private WhereOperationProcessor whereOperationGenerator;

  @Before
  public void setUp() throws Exception {
    this.schemaFile = new File(testGraphqlSchemaLocation);
    this.schemaParser = new SchemaParser();
    this.serializer = new DefaultGraphqlSerializer();
    this.expectedTypeDefinitionRegistry = schemaParser.parse(schemaFile);
    this.whereOperationGenerator = new WhereOperationProcessor();
  }

  @Test
  public void testAttachOperation() {
    List<ObjectTypeDefinition> definitions =
        expectedTypeDefinitionRegistry
            .getTypes(ObjectTypeDefinition.class)
            .stream()
            .map(
                obj -> whereOperationGenerator.attachOperation(obj, expectedTypeDefinitionRegistry))
            .collect(Collectors.toList());
    definitions.stream().map(serializer::serialize).forEach(System.out::println);
  }

  @Test
  public void testGenerateOperation() {

    List<InputObjectTypeDefinition> definitions =
        expectedTypeDefinitionRegistry
            .getTypes(ObjectTypeDefinition.class)
            .stream()
            .map(obj -> whereOperationGenerator
                .generateOperation(obj, expectedTypeDefinitionRegistry))
            .collect(Collectors.toList());

    definitions.stream().map(serializer::serialize).forEach(System.out::println);
  }
}
