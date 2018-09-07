package com.gqlolap.hog.pipeline.initialize;

import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/** Created by weijialuo on 7/2/18. */
public class DefaultInitializer implements Initializer {

  @Override
  public InitializedEnvironment initialize(TypeDefinitionRegistry sourceRegistry) {

    return new InitializedEnvironment(sourceRegistry, generateStaticSchema());
  }

  protected TypeDefinitionRegistry generateStaticSchema() {
    InputStream in = getClass().getResourceAsStream("/hogs.graphqls");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    SchemaParser schemaParser = new SchemaParser();
    return schemaParser.parse(reader);
  }
}
