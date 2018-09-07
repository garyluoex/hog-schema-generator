package com.gqlolap.hog.serializer;

import com.gqlolap.hog.Component;
import graphql.schema.idl.TypeDefinitionRegistry;

public interface SchemaSerializer extends Component {

  String serialize(TypeDefinitionRegistry typeDefinitionRegistry);

}
