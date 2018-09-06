package com.oath.hogs.pipeline.generate;

import com.oath.hogs.processor.Processor;
import com.oath.hogs.processor.StaticProcessor;
import com.oath.hogs.processor.where.WhereOperationProcessor;
import graphql.schema.idl.TypeDefinitionRegistry;
import com.oath.hogs.pipeline.validate.ValidatedEnvironment;
import java.util.Arrays;
import java.util.List;

/** Created by weijialuo on 7/2/18. */
public class DefaultGenerator implements Generator {

  @Override
  public GeneratedEnvironment generate(final ValidatedEnvironment validatedEnvironment) {

    TypeDefinitionRegistry generatedTypeDefinitionRegistry = validatedEnvironment.getSourceTypeDefinitionRegistry().merge(validatedEnvironment.getStaticTypeDefinitionRegistry());

    GeneratedEnvironment generatedEnvironment = new GeneratedEnvironment(validatedEnvironment, generatedTypeDefinitionRegistry);



    List<Processor> processorList = Arrays.asList(new WhereOperationProcessor(),  new StaticProcessor());

    for (Processor processor : validatedEnvironment.getConfiguredProcessors().values()) {
      generatedEnvironment = processor.generate(generatedEnvironment);
    }

    return generatedEnvironment;
  }
}

