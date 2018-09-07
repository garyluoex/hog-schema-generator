package com.gqlolap.hog.adapter;

import com.gqlolap.hog.Component;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.List;

/** Created by weijialuo on 7/2/18. */
public interface InputAdapter extends Component {

  TypeDefinitionRegistry getSourceTypeDefinitionRegistry();
}
