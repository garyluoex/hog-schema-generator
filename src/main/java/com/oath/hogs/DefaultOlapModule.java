package com.oath.hogs;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.oath.hogs.pipeline.certify.Certifier;
import com.oath.hogs.pipeline.certify.DefaultCertifier;
import com.oath.hogs.pipeline.config.Configurator;
import com.oath.hogs.pipeline.config.DefaultConfigurator;
import com.oath.hogs.pipeline.generate.DefaultGenerator;
import com.oath.hogs.pipeline.generate.Generator;
import com.oath.hogs.pipeline.initialize.DefaultInitializer;
import com.oath.hogs.pipeline.initialize.Initializer;
import com.oath.hogs.pipeline.validate.DefaultValidator;
import com.oath.hogs.pipeline.validate.Validator;
import com.oath.hogs.processor.Processor;
import com.oath.hogs.processor.StaticProcessor;
import com.oath.hogs.processor.where.WhereOperationProcessor;
import com.oath.hogs.serializer.GraphqlSchemaPrinter;
import com.oath.hogs.serializer.SchemaSerializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Binding actual processors to each phase of com.oath.hogs.pipeline with overridable binding
 * methods.
 */
public class DefaultOlapModule extends AbstractModule {

  Map<Class, Class> bindingMapping;
  List<Class> processors;

  public DefaultOlapModule() {
    bindingMapping = new HashMap<>();
    processors = new ArrayList<>();
  }

  @Override
  protected void configure() {
    MapBinder<String, Processor> processorBinder =
        MapBinder.newMapBinder(binder(), String.class, Processor.class);

    processors.forEach(processor -> processorBinder.addBinding(processor.getSimpleName()).to(processor));

    bindingMapping.entrySet().stream().forEach(entry -> bind(entry.getKey()).to(entry.getValue()));
  }

  public void addBinding(Class sourceClass, Class targetClass) {
    bindingMapping.put(sourceClass, targetClass);
  }

  public void addProcessor(Class processorClass) {
    processors.add(processorClass);
  }

  public DefaultOlapModule useDefaultBindings() {
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
