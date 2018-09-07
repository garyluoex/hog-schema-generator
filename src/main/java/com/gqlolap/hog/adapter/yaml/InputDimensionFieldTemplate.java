package com.gqlolap.hog.adapter.yaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InputDimensionFieldTemplate {

  private String apiName;
  private String type;
  private String domain;
  private String description;

  public String getApiName() {
    return apiName;
  }

  public String getType() {
    return type;
  }

  public String getDomain() {
    return domain;
  }

  public String getDescription() {
    return description;
  }

  public void setApiName(String apiName) {
    this.apiName = apiName;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
