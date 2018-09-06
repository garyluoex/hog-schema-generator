package com.oath.hogs;

import graphql.language.Definition;
import graphql.language.TypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestingHelperMethods {


  public static Boolean containsAllString(List<String> generatedResult, String expectedSource) {

    String cleanedSource = expectedSource.replace("\n", "").replace(" ", "").replace("\t", "");

    return generatedResult.stream()
        .map(str -> str.replace("\n", "").replace(" ", "").replace("\t", ""))
        .allMatch(cleanedSource::contains);
  }

  public static String readFile(File file) throws IOException {
    FileReader reader = new FileReader(file);
    char[] buffer = new char[1024 * 4];
    StringWriter sw = new StringWriter();
    int n;
    while (-1 != (n = reader.read(buffer))) {
      sw.write(buffer, 0, n);
    }
    return sw.toString();
  }

  public static List<Definition> getAllDefinitions(TypeDefinitionRegistry typeDefinitionRegistry) {
    List<Definition> allTypeDefs =
        new ArrayList<>(typeDefinitionRegistry.getTypes(TypeDefinition.class));

    Stream.of(
        typeDefinitionRegistry.objectTypeExtensions().values().stream(),
        typeDefinitionRegistry.enumTypeExtensions().values().stream(),
        typeDefinitionRegistry.inputObjectTypeExtensions().values().stream(),
        typeDefinitionRegistry.interfaceTypeExtensions().values().stream())
        .flatMap(Function.identity())
        .forEach(allTypeDefs::addAll);

    typeDefinitionRegistry.schemaDefinition().ifPresent(allTypeDefs::add);

    return allTypeDefs;
  }
}
