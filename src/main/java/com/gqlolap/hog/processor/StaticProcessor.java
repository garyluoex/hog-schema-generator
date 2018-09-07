package com.gqlolap.hog.processor;

import com.gqlolap.hog.HelperMethods;
import com.gqlolap.hog.pipeline.generate.GeneratedEnvironment;
import graphql.language.FieldDefinition;
import graphql.language.InterfaceTypeDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StaticProcessor implements Processor {

  @Override
  public GeneratedEnvironment generate(GeneratedEnvironment generatedEnvironment) {

    TypeDefinitionRegistry generatedTypeDefinitionRegistry = generatedEnvironment
        .getGeneratedTypeDefinitionRegistry();

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
                    generatedTypeDefinitionRegistry.getType(type, InterfaceTypeDefinition.class)
                        .get())
            .collect(Collectors.toList());

    List<ObjectTypeDefinition> implementations =
        interfaceDefinition
            .stream()
            .map(def -> HelperMethods.getImplementationsOf(def, generatedTypeDefinitionRegistry))
            .flatMap(List::stream)
            .collect(Collectors.toList());

    TypeDefinitionRegistry newTypeDefinitionRegistry = new TypeDefinitionRegistry();

    implementations
        .stream()
        .map(this::generateInterfaceField)
        .forEach(newTypeDefinitionRegistry::add);

    newTypeDefinitionRegistry.merge(HelperMethods.findAllObjectDefNotImplmentationOf(
        generatedTypeDefinitionRegistry, abstractTypes));

    return generatedEnvironment.withGeneratedTypeDeifnitionRegistry(newTypeDefinitionRegistry);
  }

  private ObjectTypeDefinition generateInterfaceField(ObjectTypeDefinition sourceDefinition) {
    FieldDefinition fieldDefinition = new FieldDefinition("_HOGSType", new TypeName("HOGSType"));

    List<FieldDefinition> newFieldDef = new ArrayList<>(sourceDefinition.getFieldDefinitions());
    newFieldDef.add(fieldDefinition);

    return new ObjectTypeDefinition(
        sourceDefinition.getName(),
        sourceDefinition.getImplements(),
        Collections.emptyList(),
        newFieldDef);
  }
}
