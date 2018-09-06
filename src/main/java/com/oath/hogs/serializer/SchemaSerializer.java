package com.oath.hogs.serializer;

import graphql.schema.idl.TypeDefinitionRegistry;

public interface SchemaSerializer {

  String serialize(TypeDefinitionRegistry typeDefinitionRegistry);

}
