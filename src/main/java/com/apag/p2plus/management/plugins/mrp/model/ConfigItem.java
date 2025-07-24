package com.apag.p2plus.management.plugins.mrp.model;

/**
 * Represents a configuration item with name, type, value and description
 */
public class ConfigItem {

  private String name;
  private String type;
  private Object value;
  private String description;

  /**
   * Default constructor for JSON deserialization
   */
  public ConfigItem() {
  }

  public ConfigItem(String name, String type, Object value, String description) {
    this.name = name;
    this.type = type;
    this.value = value;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Helper method to get value as String
   */
  public String getValueAsString() {
    return value != null ? value.toString() : "";
  }

  /**
   * Helper method to get value as Boolean
   */
  public boolean getValueAsBoolean() {
    if (value instanceof Boolean) {
      return (Boolean) value;
    } else if (value instanceof String) {
      return Boolean.parseBoolean((String) value);
    }
    return false;
  }

  @Override
  public String toString() {
    return "ConfigItem{" +
           "name='" + name + '\'' +
           ", type='" + type + '\'' +
           ", value=" + value +
           ", description='" + description + '\'' +
           '}';
  }
} 