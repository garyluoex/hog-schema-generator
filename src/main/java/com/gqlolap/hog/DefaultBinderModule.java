package com.gqlolap.hog;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.gqlolap.hog.pipeline.certify.Certifier;
import com.gqlolap.hog.pipeline.certify.DefaultCertifier;
import com.gqlolap.hog.pipeline.config.Configurator;
import com.gqlolap.hog.pipeline.config.DefaultConfigurator;
import com.gqlolap.hog.pipeline.generate.DefaultGenerator;
import com.gqlolap.hog.pipeline.generate.Generator;
import com.gqlolap.hog.pipeline.initialize.DefaultInitializer;
import com.gqlolap.hog.pipeline.initialize.Initializer;
import com.gqlolap.hog.pipeline.validate.DefaultValidator;
import com.gqlolap.hog.pipeline.validate.Validator;
import com.gqlolap.hog.processor.Processor;
import com.gqlolap.hog.processor.StaticProcessor;
import com.gqlolap.hog.processor.where.WhereOperationProcessor;
import com.gqlolap.hog.serializer.GraphqlSchemaPrinter;
import com.gqlolap.hog.serializer.SchemaSerializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Binding actual processors to each phase of pipeline with overridable binding methods.
 */
public class DefaultBinderModule extends AbstractModule {

  Map<Class<? extends Component>, Class> bindingMapping;
  List<Class<? extends Processor>> processors;

  /**
   * Constructor.
   */
  public DefaultBinderModule() {
    bindingMapping = new HashMap<>();
    processors = new ArrayList<>();
  }

  @Override
  protected void configure() {
    MapBinder<String, Processor> processorBinder =
        MapBinder.newMapBinder(binder(), String.class, Processor.class);

    processors
        .forEach(processor -> processorBinder.addBinding(processor.getSimpleName()).to(processor));

    bindingMapping.forEach((key, value) -> bind(key).to(value));
  }

  public void addBinding(Class sourceClass, Class targetClass) {
    bindingMapping.put(sourceClass, targetClass);
  }

  public void addProcessor(Class<? extends Processor> processorClass) {
    processors.add(processorClass);
  }

  /**
   * Use default pipeline components.
   *
   * @return This binding module.
   */
  public DefaultBinderModule useDefaultBindings() {
    addProcessor(WhereOperationProcessor.class);
    addProcessor(StaticProcessor.class);
    addBinding(Initializer.class, DefaultInitializer.class);
    addBinding(Configurator.class, DefaultConfigurator.class);
    addBinding(Validator.class, DefaultValidator.class);
    addBinding(Generator.class, DefaultGenerator.class);
    addBinding(Certifier.class, DefaultCertifier.class);
    addBinding(SchemaSerializer.class, GraphqlSchemaPrinter.class);

    return this;
  }
}
