package com.gqlolap.hog.adapter;

import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** Parses GraphQL schema definition into type TypeDefinitionRegistry. */
public class GraphqlInputAdapter implements InputAdapter {

  private List<File> inputGraphqlSchema;

  public GraphqlInputAdapter(File... inputGraphqlSchema) {
    this.inputGraphqlSchema = Arrays.asList(inputGraphqlSchema);
  }

  @Override
  public TypeDefinitionRegistry getSourceTypeDefinitionRegistry() {
    SchemaParser schemaParser = new SchemaParser();
    List<TypeDefinitionRegistry> typeDefinitionRegistries =
        inputGraphqlSchema.stream().map(schemaParser::parse).collect(Collectors.toList());

    TypeDefinitionRegistry mergedTypeDefinitionRegistry = new TypeDefinitionRegistry();

    typeDefinitionRegistries.forEach(mergedTypeDefinitionRegistry::merge);

    return mergedTypeDefinitionRegistry;
  }
}
