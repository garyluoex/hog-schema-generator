package com.oath.hogs.adapter;

import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.List;

/** Created by weijialuo on 7/2/18. */
public interface InputAdapter {

  TypeDefinitionRegistry getSourceTypeDefinitionRegistry();
}
