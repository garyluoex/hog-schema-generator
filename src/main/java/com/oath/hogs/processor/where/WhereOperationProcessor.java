package com.oath.hogs.processor.where;

import com.oath.hogs.HelperMethods;
import com.oath.hogs.processor.Processor;
import com.oath.hogs.pipeline.generate.GeneratedEnvironment;
import graphql.language.FieldDefinition;
import graphql.language.InputObjectTypeDefinition;
import graphql.language.InputValueDefinition;
import graphql.language.InterfaceTypeDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WhereOperationProcessor implements Processor {

  @Override
  public GeneratedEnvironment generate(GeneratedEnvironment generatedEnvironment) {

    TypeDefinitionRegistry generatedTypeDefinitionRegistry =
        generatedEnvironment.getGeneratedTypeDefinitionRegistry();

    List<Type> abstractTypes =
        Arrays.asList("HOGSDimension", "HOGSDatasources", "HOGSRecord", "HOGSMetric")
            .stream()
            .map(TypeName::new)
            .collect(Collectors.toList());

    List<InterfaceTypeDefinition> interfaceDefinition =
        abstractTypes
            .stream()
            .map(
                type ->
                    generatedEnvironment
                        .getGeneratedTypeDefinitionRegistry()
                        .getType(type, InterfaceTypeDefinition.class)
                        .get())
            .collect(Collectors.toList());

    List<ObjectTypeDefinition> implementations =
        interfaceDefinition
            .stream()
            .map(def -> HelperMethods.getImplementationsOf(def, generatedTypeDefinitionRegistry))
            .flatMap(List::stream)
            .collect(Collectors.toList());

    TypeDefinitionRegistry resultTypeDefinitionRegistry = new TypeDefinitionRegistry();

    implementations
        .stream()
        .map(objDef -> generateOperation(objDef, generatedTypeDefinitionRegistry))
        .forEach(resultTypeDefinitionRegistry::add);
    implementations
        .stream()
        .map(objDef -> attachOperation(objDef, generatedTypeDefinitionRegistry))
        .forEach(resultTypeDefinitionRegistry::add);

    resultTypeDefinitionRegistry.merge(
        HelperMethods.findAllObjectDefNotImplmentationOf(
            generatedEnvironment.getGeneratedTypeDefinitionRegistry(), abstractTypes));

    return generatedEnvironment.withGeneratedTypeDeifnitionRegistry(resultTypeDefinitionRegistry);
  }

  public ObjectTypeDefinition attachOperation(
      ObjectTypeDefinition sourceDefinition, TypeDefinitionRegistry typeDefinitionRegistry) {

    List<FieldDefinition> attachedFieldDefinitions =
        sourceDefinition
            .getFieldDefinitions()
            .stream()
            .map(field -> attachOperationField(field, typeDefinitionRegistry))
            .collect(Collectors.toList());

    return new ObjectTypeDefinition(
        sourceDefinition.getName(),
        sourceDefinition.getImplements(),
        Collections.emptyList(),
        attachedFieldDefinitions);
  }

  public InputObjectTypeDefinition generateOperation(
      ObjectTypeDefinition sourceDefinition, TypeDefinitionRegistry typeDefinitionRegistry) {

    String name = whereOperationInputDefinitionName(sourceDefinition.getName());
    List<InputValueDefinition> inputValueDefinitions =
        sourceDefinition
            .getFieldDefinitions()
            .stream()
            .map(field -> generateInputValueDefinition(field, typeDefinitionRegistry))
            .flatMap(List::stream)
            .collect(Collectors.toList());

    return new InputObjectTypeDefinition(name, Collections.emptyList(), inputValueDefinitions);
  }

  private List<InputValueDefinition> generateInputValueDefinition(
      FieldDefinition fieldDefinition, TypeDefinitionRegistry typeDefinitionRegistry) {
    String name = fieldDefinition.getName();
    Type sourceType = fieldDefinition.getType();
    Type operationType =
        typeDefinitionRegistry.isObjectType(sourceType)
            ? new TypeName(
                whereOperationInputDefinitionName(HelperMethods.serializeType(HelperMethods.innerMostType(sourceType))))
            : sourceType;

    return Arrays.stream(DefaultAllowedWhereOperations.values())
        .filter(operation -> operation.validOnType(sourceType, typeDefinitionRegistry))
        .map(DefaultAllowedWhereOperations::name)
        .map(operationName -> whereOperationFieldName(name, operationName))
        .map(inputFieldName -> new InputValueDefinition(inputFieldName, operationType))
        .collect(Collectors.toList());
  }

  private String whereOperationFieldName(String fieldName, String operation) {
    return operation.startsWith("_") ? fieldName + operation : operation + fieldName;
  }

  private String whereOperationInputDefinitionName(String objectName) {
    return objectName + "WhereOperation";
  }

  private FieldDefinition attachOperationField(
      FieldDefinition fieldDefinition, TypeDefinitionRegistry typeDefinitionRegistry) {

    String name = "where";
    Type sourceType = fieldDefinition.getType();

    // If the field is a scalar type, skip
    if (!typeDefinitionRegistry.isObjectType(HelperMethods.innerMostType(sourceType))) {
      return fieldDefinition;
    }

    // If the field name starts with underscore, skip
    if (fieldDefinition.getName().startsWith("_")) {
      return fieldDefinition;
    }

    // If the field is not HOGS type, skip
    ObjectTypeDefinition objectTypeDefinition =
        typeDefinitionRegistry.getType(sourceType, ObjectTypeDefinition.class).get();
    List<String> interfaces =
        objectTypeDefinition
            .getImplements()
            .stream()
            .map(HelperMethods::serializeType)
            .collect(Collectors.toList());
    if (interfaces.stream().noneMatch(str -> str.contains("HOGS"))) {
      return fieldDefinition;
    }

    Type whereOperationInputType =
        new TypeName(
            whereOperationInputDefinitionName(HelperMethods.innerMostType(sourceType).getName()));

    List<InputValueDefinition> arguments = fieldDefinition.getInputValueDefinitions();

    InputValueDefinition whereOperationArgument =
        new InputValueDefinition(name, whereOperationInputType);

    arguments.add(whereOperationArgument);

    return new FieldDefinition(
        fieldDefinition.getName(), fieldDefinition.getType(), arguments, Collections.emptyList());
  }
}
