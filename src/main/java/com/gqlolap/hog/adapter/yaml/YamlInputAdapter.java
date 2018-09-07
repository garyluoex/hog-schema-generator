package com.gqlolap.hog.adapter.yaml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gqlolap.hog.HelperMethods;
import com.gqlolap.hog.adapter.InputAdapter;
import graphql.language.Description;
import graphql.language.FieldDefinition;
import graphql.language.ListType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class YamlInputAdapter implements InputAdapter {

  private ObjectMapper mapper;
  private List<File> datasources;
  private List<File> dimensions;
  private List<File> metrics;

  /**
   * TODO.
   *
   * @param datasources TODO.
   * @param dimensions TODO.
   * @param metrics TODO.
   */
  public YamlInputAdapter(List<File> datasources, List<File> dimensions, List<File> metrics) {
    this.mapper = new ObjectMapper(new YAMLFactory());
    this.datasources = datasources;
    this.dimensions = dimensions;
    this.metrics = metrics;
  }

  @Override
  public TypeDefinitionRegistry getSourceTypeDefinitionRegistry() {

    String datasourceYaml =
        datasources.stream().map(HelperMethods::readFile).collect(Collectors.joining("\n"));

    String dimensionYaml =
        dimensions.stream().map(HelperMethods::readFile).collect(Collectors.joining("\n"));

    String metricYaml =
        metrics.stream().map(HelperMethods::readFile).collect(Collectors.joining("\n"));

    Map<String, InputDatasourceTemplate> inputDatasourceObjects = null;
    Map<String, InputDimensionTemplate> inputDimensionObjects = null;
    Map<String, InputMetricTemplate> inputMetricObjects = null;

    try {
      inputDatasourceObjects =
          mapper.readValue(
              datasourceYaml, new TypeReference<Map<String, InputDatasourceTemplate>>() {
              });
      inputDimensionObjects =
          mapper.readValue(
              dimensionYaml, new TypeReference<Map<String, InputDimensionTemplate>>() {
              });
      inputMetricObjects =
          mapper.readValue(metricYaml, new TypeReference<Map<String, InputMetricTemplate>>() {
          });

    } catch (IOException e) {
      e.printStackTrace();
    }

    TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();

    parseDatasourceDefinitions(inputDatasourceObjects).forEach(typeDefinitionRegistry::add);

    parseDimensionDefinitions(inputDimensionObjects).forEach(typeDefinitionRegistry::add);

    parseMetricDefinitions(inputMetricObjects).forEach(typeDefinitionRegistry::add);

    typeDefinitionRegistry.add(generateDatasourcesDefinition(inputDatasourceObjects));

    return typeDefinitionRegistry;
  }

  private ObjectTypeDefinition generateDatasourcesDefinition(
      Map<String, InputDatasourceTemplate> inputDatasourceTemplates) {
    String name = "Datasources";
    List<Type> type = Collections.singletonList(new TypeName("HOGSDatasources"));

    List<FieldDefinition> fieldDefinitions = inputDatasourceTemplates.values().stream()
        .map(this::generateDatasourcesFieldDefinition).collect(Collectors.toList());
    return new ObjectTypeDefinition(name, type, Collections.emptyList(), fieldDefinitions);
  }

  private FieldDefinition generateDatasourcesFieldDefinition(
      InputDatasourceTemplate inputDatasourceTemplate) {

    return new FieldDefinition(inputDatasourceTemplate.getApiName(),
        new ListType(new TypeName(inputDatasourceTemplate.getApiName() + "Record")));
  }

  private List<ObjectTypeDefinition> parseDatasourceDefinitions(
      Map<String, InputDatasourceTemplate> inputObjects) {
    return inputObjects
        .values()
        .stream()
        .map(this::parseDatasourceDefinition)
        .collect(Collectors.toList());
  }

  private ObjectTypeDefinition parseDatasourceDefinition(InputDatasourceTemplate inputObject) {
    List<FieldDefinition> fieldDefinitions =
        parseDatasourceFields(inputObject.getFields());
    ObjectTypeDefinition result =
        new ObjectTypeDefinition(
            inputObject.getApiName() + "Record",
            Collections.singletonList(new TypeName("HOGSRecord")),
            Collections.emptyList(),
            fieldDefinitions);
    result.setDescription(new Description(inputObject.getDescription(), null, false));
    return result;
  }

  private List<FieldDefinition> parseDatasourceFields(
      Map<String, InputRecordFieldTemplate> recordFieldTemplateMap) {
    return recordFieldTemplateMap
        .values()
        .stream()
        .map(this::parseDatasourceField)
        .collect(Collectors.toList());
  }

  private FieldDefinition parseDatasourceField(InputRecordFieldTemplate inputRecordFieldTemplate) {
    FieldDefinition result =
        new FieldDefinition(
            inputRecordFieldTemplate.getApiName(),
            new TypeName(inputRecordFieldTemplate.getDomain()));
    result.setDescription(new Description(inputRecordFieldTemplate.getDescription(), null, false));
    return result;
  }

  private List<ObjectTypeDefinition> parseMetricDefinitions(
      Map<String, InputMetricTemplate> inputObjects) {
    return inputObjects
        .values()
        .stream()
        .map(this::parseMetricDefinition)
        .collect(Collectors.toList());
  }

  private ObjectTypeDefinition parseMetricDefinition(InputMetricTemplate inputObject) {
    List<FieldDefinition> fieldDefinitions =
        parseMetricFields(inputObject.getFields());
    ObjectTypeDefinition result =
        new ObjectTypeDefinition(
            inputObject.getApiName(),
            Collections.singletonList(new TypeName(inputObject.getType())),
            Collections.emptyList(),
            fieldDefinitions);
    return result;
  }

  private List<FieldDefinition> parseMetricFields(
      Map<String, InputMetricFieldTemplate> metricFieldTemplateMap) {
    return metricFieldTemplateMap
        .values()
        .stream()
        .map(this::parseMetricField)
        .collect(Collectors.toList());
  }

  private FieldDefinition parseMetricField(InputMetricFieldTemplate inputMetricFieldTemplate) {
    FieldDefinition result =
        new FieldDefinition(
            inputMetricFieldTemplate.getApiName(),
            new TypeName(inputMetricFieldTemplate.getType()));
    result.setDescription(new Description(inputMetricFieldTemplate.getDescription(), null, false));
    return result;
  }

  private List<ObjectTypeDefinition> parseDimensionDefinitions(
      Map<String, InputDimensionTemplate> inputObjects) {

    return inputObjects
        .entrySet()
        .stream()
        .map(entry -> parseDimensionDefinition(entry.getValue()))
        .collect(Collectors.toList());
  }

  private ObjectTypeDefinition parseDimensionDefinition(InputDimensionTemplate inputObject) {
    String apiName = inputObject.getApiName();
    List<Type> implementz = Collections.singletonList(new TypeName(inputObject.getType()));
    List<FieldDefinition> fields = parseFields(inputObject.getFields());

    return new ObjectTypeDefinition(apiName, implementz, Collections.emptyList(), fields);
  }

  private List<FieldDefinition> parseFields(Map<String, InputDimensionFieldTemplate> fields) {
    return fields
        .entrySet()
        .stream()
        .map(entry -> parseField(entry.getValue()))
        .collect(Collectors.toList());
  }

  private FieldDefinition parseField(InputDimensionFieldTemplate contents) {
    String typeName = contents.getType();

    if (contents.getDomain() != null) {
      typeName = contents.getDomain();
    }

    return new FieldDefinition(contents.getApiName(), new TypeName(typeName));
  }
}
