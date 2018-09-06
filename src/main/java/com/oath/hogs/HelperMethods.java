package com.oath.hogs;

import graphql.language.Definition;
import graphql.language.InterfaceTypeDefinition;
import graphql.language.ListType;
import graphql.language.NonNullType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
import graphql.language.TypeDefinition;
import graphql.language.TypeName;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelperMethods {

  public static String readFile(File file) {
    try {
    FileReader reader = new FileReader(file);
    char[] buffer = new char[1024 * 4];
    StringWriter sw = new StringWriter();
    int n;
    while (-1 != (n = reader.read(buffer))) {
      sw.write(buffer, 0, n);
    }
    return sw.toString();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Error while reading input file.", e);
    }
  }

  public static String serializeType(Type type) {
    if (type instanceof ListType) {
      ListType currentType = (ListType) type;
      return String.format("[%s]", serializeType(currentType.getType()));
    }

    if (type instanceof NonNullType) {
      NonNullType currentType = (NonNullType) type;
      return String.format("%s!", serializeType(currentType.getType()));
    }

    if (type instanceof TypeName) {
      TypeName currentType = (TypeName) type;
      return currentType.getName();
    }

    return null;
  }

  public static TypeName innerMostType(Type type) {
    if (type instanceof ListType) {
      ListType currentType = (ListType) type;
      return innerMostType(currentType.getType());
    }

    if (type instanceof NonNullType) {
      NonNullType currentType = (NonNullType) type;
      return innerMostType(currentType.getType());
    }

    if (type instanceof TypeName) {
      TypeName currentType = (TypeName) type;
      return currentType;
    }

    return null;
  }


  public static TypeDefinitionRegistry findAllObjectDefNotImplmentationOf(
      TypeDefinitionRegistry sourceTypeDefReg, Type type) {

    return HelperMethods.findAllObjectDefNotImplmentationOf(sourceTypeDefReg, Collections.singletonList(type));
  }

  public static TypeDefinitionRegistry findAllObjectDefNotImplmentationOf(
      TypeDefinitionRegistry sourceTypeDefReg, List<Type> types) {

    TypeDefinitionRegistry targetTypeDefReg = new TypeDefinitionRegistry();

    List<String> typeNames = types.stream().map(HelperMethods::serializeType).collect(Collectors.toList());

    sourceTypeDefReg
        .getTypes(TypeDefinition.class)
        .stream()
        .filter(
            definition -> {
              if (definition instanceof ObjectTypeDefinition) {
                return ((ObjectTypeDefinition) definition).getImplements().stream().map(HelperMethods::serializeType).noneMatch(typeNames::contains);
              }
              return true;
            })
        .forEach(targetTypeDefReg::add);

    List<TypeDefinition> extendTypeDefinitions = new ArrayList<>();

    Stream.of(
        sourceTypeDefReg.objectTypeExtensions().values().stream(),
        sourceTypeDefReg.enumTypeExtensions().values().stream(),
        sourceTypeDefReg.inputObjectTypeExtensions().values().stream(),
        sourceTypeDefReg.interfaceTypeExtensions().values().stream())
        .flatMap(Function.identity())
        .forEach(extendTypeDefinitions::addAll);

    extendTypeDefinitions.forEach(targetTypeDefReg::add);

    sourceTypeDefReg.schemaDefinition().ifPresent(schema -> targetTypeDefReg.add(schema));

    return targetTypeDefReg;
  }

  public static List<ObjectTypeDefinition> getImplementationsOf(
      InterfaceTypeDefinition targetInterface, TypeDefinitionRegistry typeDefinitionRegistry) {
    List<ObjectTypeDefinition> objectTypeDefinitions =
        typeDefinitionRegistry.getTypes(ObjectTypeDefinition.class);
    return objectTypeDefinitions
        .stream()
        .filter(
            objectTypeDefinition -> {
              List<Type> implementsList = objectTypeDefinition.getImplements();
              for (Type iFace : implementsList) {
                boolean equals = innerMostType(iFace).getName().equals(targetInterface.getName());
                if (equals) {
                  return true;
                }
              }
              return false;
            })
        .collect(Collectors.toList());
  }

  public static String camelCase(String s) {
    String[] words = s.toLowerCase(Locale.ENGLISH).split("_");
    StringBuilder lowerCamelCase = new StringBuilder(words[0]);

    for (int i = 1; i < words.length; i++) {
      lowerCamelCase.append((words[i].substring(0, 1)).toUpperCase(Locale.ENGLISH));
      lowerCamelCase.append(words[i].substring(1));
    }
    return lowerCamelCase.toString();
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
