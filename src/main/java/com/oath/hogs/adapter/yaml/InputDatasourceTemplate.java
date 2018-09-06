package com.oath.hogs.adapter.yaml;

import java.util.Map;

public class InputDatasourceTemplate {

  private String apiName;
  private String type;
  private String description;
  private Map<String, InputRecordFieldTemplate> fields;

  public String getApiName() {
    return apiName;
  }

  public String getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  public Map<String, InputRecordFieldTemplate> getFields() {
    return fields;
  }

  public void setApiName(String apiName) {
    this.apiName = apiName;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setFields(
      Map<String, InputRecordFieldTemplate> fields) {
    this.fields = fields;
  }
}
