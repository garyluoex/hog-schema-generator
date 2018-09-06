package com.oath.hogs.adapter.yaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InputDimensionTemplate {

  private String apiName;
  private String type;
  private String description;
  private Map<String, InputDimensionFieldTemplate> fields;

  public String getApiName() {
    return apiName;
  }

  public void setApiName(String apiName) {
    this.apiName = apiName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public Map<String, InputDimensionFieldTemplate> getFields() {
    return fields;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setFields(Map<String, InputDimensionFieldTemplate> fields) {
    this.fields = fields;
  }
}
