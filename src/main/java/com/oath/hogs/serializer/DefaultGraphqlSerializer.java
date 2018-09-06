package com.oath.hogs.serializer;

import com.oath.hogs.HelperMethods;
import graphql.language.Definition;
import graphql.language.EnumTypeDefinition;
import graphql.language.EnumTypeExtensionDefinition;
import graphql.language.EnumValueDefinition;
import graphql.language.FieldDefinition;
import graphql.language.InputObjectTypeDefinition;
import graphql.language.InputObjectTypeExtensionDefinition;
import graphql.language.InputValueDefinition;
import graphql.language.InterfaceTypeDefinition;
import graphql.language.InterfaceTypeExtensionDefinition;
import graphql.language.ListType;
import graphql.language.NonNullType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.ObjectTypeExtensionDefinition;
import graphql.language.OperationTypeDefinition;
import graphql.language.SchemaDefinition;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultGraphqlSerializer implements SchemaSerializer {


  @Override
  public String serialize(TypeDefinitionRegistry typeDefinitionRegistry) {
    return HelperMethods.getAllDefinitions(typeDefinitionRegistry)
        .stream()
        .map(this::serialize)
        .collect(Collectors.joining("\n"));
  }

  /**
   * Serialize the given TypeDefinition into graphql schema definition language representation.
   *
   * <p>TODO: case where class extend multiple definitions, fix logging on no supported * definition
   * type
   *
   * @param sourceDefinition the TypeDefinition to be serialized.
   * @return string of TypeDefinition in graphql schema definition language representation.
   */
  public String serialize(Definition sourceDefinition) {

    if (sourceDefinition instanceof ObjectTypeExtensionDefinition) {
      return "extend " + serialize((ObjectTypeDefinition) sourceDefinition);
    }

    if (sourceDefinition instanceof InterfaceTypeExtensionDefinition) {
      return "extend " + serialize((InterfaceTypeDefinition) sourceDefinition);
    }

    if (sourceDefinition instanceof InputObjectTypeExtensionDefinition) {
      return "extend " + serialize((InputObjectTypeDefinition) sourceDefinition);
    }

    if (sourceDefinition instanceof EnumTypeExtensionDefinition) {
      return "extend " + serialize((EnumTypeDefinition) sourceDefinition);
    }

    if (sourceDefinition instanceof ObjectTypeDefinition) {
      return serialize((ObjectTypeDefinition) sourceDefinition);
    }

    if (sourceDefinition instanceof InterfaceTypeDefinition) {
      return serialize((InterfaceTypeDefinition) sourceDefinition);
    }

    if (sourceDefinition instanceof InputObjectTypeDefinition) {
      return serialize((InputObjectTypeDefinition) sourceDefinition);
    }

    if (sourceDefinition instanceof EnumTypeDefinition) {
      return serialize((EnumTypeDefinition) sourceDefinition);
    }

    if (sourceDefinition instanceof SchemaDefinition) {
      return serialize((SchemaDefinition) sourceDefinition);
    }

    return sourceDefinition.toString();
  }

  /**
   * Serialize the given ObjectTypeDefinition into graphql schema definition language
   * representation.
   *
   * @param sourceDefinition the ObjectTypeDefinition to be serialized.
   * @return string of ObjectTypeDefinition in graphql schema definition language representation.
   */
  public String serialize(ObjectTypeDefinition sourceDefinition) {
    String type = "type";
    String name = sourceDefinition.getName();
    List<String> interfaces =
        sourceDefinition
            .getImplements()
            .stream()
            .map(this::serializeType)
            .collect(Collectors.toList());
    String fields =
        sourceDefinition
            .getFieldDefinitions()
            .stream()
            .map(this::serializeFieldDefinition)
            .collect(Collectors.joining("\n"));

    return buildSchema(type, name, interfaces, fields);
  }

  /**
   * Serialize the given InterfaceTypeDefinition into graphql schema definition language
   * representation.
   *
   * @param sourceDefinition the InterfaceTypeDefinition to be serialized.
   * @return string of InterfaceTypeDefinition in graphql schema definition language representation.
   */
  public String serialize(InterfaceTypeDefinition sourceDefinition) {
    String type = "interface";
    String name = sourceDefinition.getName();
    String fields =
        sourceDefinition
            .getFieldDefinitions()
            .stream()
            .map(this::serializeFieldDefinition)
            .collect(Collectors.joining("\n"));

    return buildSchema(type, name, Collections.emptyList(), fields);
  }

  /**
   * Serialize the given InputObjectTypeDefinition into graphql schema definition language
   * representation.
   *
   * @param sourceDefinition the InputObjectTypeDefinition to be serialized.
   * @return string of InputObjectTypeDefinition in graphql schema definition language
   *     representation.
   */
  public String serialize(InputObjectTypeDefinition sourceDefinition) {
    String type = "input";
    String name = sourceDefinition.getName();
    String fields =
        sourceDefinition
            .getInputValueDefinitions()
            .stream()
            .map(this::serializeInputValueDefinition)
            .collect(Collectors.joining("\n"));

    return buildSchema(type, name, Collections.emptyList(), fields);
  }

  /**
   * Serialize the given EnumTypeDefinition into graphql schema definition language representation.
   *
   * @param sourceDefinition the EnumTypeDefinition to be serialized.
   * @return string of EnumTypeDefinition in graphql schema definition language representation.
   */
  public String serialize(EnumTypeDefinition sourceDefinition) {
    String type = "enum";
    String name = sourceDefinition.getName();
    String fields =
        sourceDefinition
            .getEnumValueDefinitions()
            .stream()
            .map(this::serializeEnumValueDefinition)
            .collect(Collectors.joining("\n"));

    return buildSchema(type, name, Collections.emptyList(), fields);
  }

  /**
   * Serialize the given SchemaDefinition into graphql schema definition language representation.
   *
   * @param sourceDefinition the SchemaDefinition to be serialized.
   * @return string of SchemaDefinition in graphql schema definition language representation.
   */
  public String serialize(SchemaDefinition sourceDefinition) {

    String type = "schema";
    String fields =
        sourceDefinition
            .getOperationTypeDefinitions()
            .stream()
            .map(this::serializeOperationTypeDefinition)
            .collect(Collectors.joining("\n"));

    return buildSchema(type, null, Collections.emptyList(), fields);
  }

  protected String buildSchema(String type, String name, List<String> interfaces, String fields) {

    String title = type + " ";
    String potentialName = name == null || name.isEmpty() ? "" : name + " ";

    String implement =
        interfaces.isEmpty()
            ? "{\n"
            : String.format("implements %s {\n", String.join(", ", interfaces));
    String closing = "\n}";

    return title + potentialName + implement + fields + closing;
  }

  protected String serializeType(Type type) {
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

  protected String serializeFieldDefinition(FieldDefinition fieldDefinition) {
    String name = fieldDefinition.getName();
    String type = serializeType(fieldDefinition.getType());
    List<InputValueDefinition> inputValueDefinitions = fieldDefinition.getInputValueDefinitions();
    String aruguments =
        inputValueDefinitions.isEmpty()
            ? ""
            : "("
                + inputValueDefinitions
                    .stream()
                    .map(source -> serializeInputValueDefinition(source , 0))
                    .collect(Collectors.joining(", "))
                + ")";

    String result = String.format("    %s%s: %s", name, aruguments, type);

    return result;
  }

  protected String serializeInputValueDefinition(InputValueDefinition inputValueDefinition) {
    return String.format(
        "    %s: %s",
        inputValueDefinition.getName(), serializeType(inputValueDefinition.getType()));
  }

  protected String serializeInputValueDefinition(
      InputValueDefinition inputValueDefinition, int indentionLevel) {

    StringBuffer indention = new StringBuffer();

    if (indentionLevel != 0) {
      for (int i = 0; i < indentionLevel; i++) {
        indention.append("    ");
      }
    }

    return String.format(
        "%s%s: %s",
        indention.toString(),
        inputValueDefinition.getName(),
        serializeType(inputValueDefinition.getType()));
  }

  protected String serializeEnumValueDefinition(EnumValueDefinition enumValueDefinition) {
    return "    " + enumValueDefinition.getName();
  }

  protected String serializeOperationTypeDefinition(
      OperationTypeDefinition operationTypeDefinition) {
    return String.format(
        "    %s: %s",
        operationTypeDefinition.getName(), serializeType(operationTypeDefinition.getType()));
  }
}
