package com.gqlolap.hog.serializer;

import com.gqlolap.hog.HelperMethods;
import graphql.language.Document;
import graphql.schema.idl.SchemaPrinter;
import graphql.schema.idl.TypeDefinitionRegistry;

public class GraphqlSchemaPrinter implements SchemaSerializer {

  @Override
  public String serialize(TypeDefinitionRegistry typeDefinitionRegistry) {
    Document document = new Document(HelperMethods.getAllDefinitions(typeDefinitionRegistry));
    SchemaPrinter schemaPrinter = new SchemaPrinter();
    return schemaPrinter.print(document);
  }
}
