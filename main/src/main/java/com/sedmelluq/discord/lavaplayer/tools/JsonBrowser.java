package com.sedmelluq.discord.lavaplayer.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Allows to easily navigate in decoded JSON data
 */
public class JsonBrowser {
  private static final ObjectMapper mapper = new ObjectMapper();

  private final Object value;

  private JsonBrowser(Object value) {
    this.value = value;
  }

  /**
   * Get an element at an index for a list value
   * @param index List index
   * @return JsonBrowser instance which wraps the value at the specified index
   */
  public JsonBrowser index(int index) {
    if (value instanceof List) {
      return new JsonBrowser(((List) value).get(index));
    } else {
      throw new IllegalStateException("Index only works on a list");
    }
  }

  /**
   * Get an element by key from a map value
   * @param key Map key
   * @return JsonBrowser instance which wraps the value with the specified key
   */
  public JsonBrowser get(Object key) {
    if (value instanceof Map) {
      return new JsonBrowser(((Map) value).get(key));
    } else {
      throw new IllegalStateException("Index only works on a list");
    }
  }

  /**
   * Get an element by key from a map value
   * @param key Map key
   * @return JsonBrowser instance which wraps the value with the specified key
   */
  public JsonBrowser safeGet(Object key) {
    if (value instanceof Map) {
      return new JsonBrowser(((Map) value).get(key));
    } else {
      return new JsonBrowser(null);
    }
  }

  /**
   * Returns a list of all the values in this element
   * @return The list of values as JsonBrowser elements
   */
  public List<JsonBrowser> values() {
    List<JsonBrowser> values = new ArrayList<>();
    if (value instanceof Map) {
      for (Object object : ((Map) value).values()) {
        values.add(new JsonBrowser(object));
      }
    } else if (value instanceof List) {
      for (Object object : (List) value) {
        values.add(new JsonBrowser(object));
      }
    }
    return values;
  }

  /**
   * Attempt to retrieve the value in the specified format
   * @param klass The class to retrieve the value as
   * @return The value as an instance of the specified class
   * @throws IllegalArgumentException If conversion is impossible
   */
  public <T> T as(Class<T> klass) {
    return mapper.convertValue(value, klass);
  }

  /**
   * @return The value of the element as text
   */
  public String text() {
    return value != null ? value.toString() : null;
  }

  /**
   * @return The value of the element as text
   */
  public boolean isNull() {
    return value == null;
  }

  /**
   * Parse from string.
   * @param json The JSON object as a string
   * @return JsonBrowser instance for navigating in the result
   * @throws IOException When parsing the JSON failed
   */
  public static JsonBrowser parse(String json) throws IOException {
    return new JsonBrowser(mapper.readValue(json, Object.class));
  }

  /**
   * Parse from string.
   * @param stream The JSON object as a stream
   * @return JsonBrowser instance for navigating in the result
   * @throws IOException When parsing the JSON failed
   */
  public static JsonBrowser parse(InputStream stream) throws IOException {
    return new JsonBrowser(mapper.readValue(stream, Object.class));
  }
}
