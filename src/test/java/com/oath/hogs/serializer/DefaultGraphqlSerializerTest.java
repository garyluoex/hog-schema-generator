package com.oath.hogs.serializer;

import static org.junit.Assert.assertTrue;

import com.oath.hogs.TestingHelperMethods;
import graphql.language.TypeDefinition;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class DefaultGraphqlSerializerTest {

  private static String testGraphqlSchemaLocation = "src/test/resources/test.graphqls";
  private File schemaFile;
  private SchemaParser schemaParser;
  private DefaultGraphqlSerializer serializer;
  private TypeDefinitionRegistry expectedTypeDefinitionRegistry;

  @Before
  public void setUp() throws Exception {
    this.schemaFile = new File(testGraphqlSchemaLocation);
    this.schemaParser = new SchemaParser();
    this.serializer = new DefaultGraphqlSerializer();
    this.expectedTypeDefinitionRegistry = schemaParser.parse(schemaFile);
  }

  @Test
  public void testSerialization() {
    List<TypeDefinition> allTypeDefinitions =
        expectedTypeDefinitionRegistry.getTypes(TypeDefinition.class);
    List<String> serializedResult =
        allTypeDefinitions.stream().map(serializer::serialize).collect(Collectors.toList());

    String originalSchema;

    try {
      originalSchema = TestingHelperMethods.readFile(schemaFile);

      assertTrue(
          "Contents of type defintion are the same",
          TestingHelperMethods.containsAllString(serializedResult, originalSchema));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
