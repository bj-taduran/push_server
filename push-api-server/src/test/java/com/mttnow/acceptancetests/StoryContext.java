package com.mttnow.acceptancetests;

import java.util.HashMap;
import java.util.Map;

public final class StoryContext {

  private static final Map<String, Object> context = new HashMap<String, Object>();

  public static void putInContext(String key, Object value) {
    context.put(key, value);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getFromContext(String key) {
    return (T) context.get(key);
  }

  public static boolean containsKey(String key) {
    return context.containsKey(key);
  }

  private StoryContext() {
  }
}
