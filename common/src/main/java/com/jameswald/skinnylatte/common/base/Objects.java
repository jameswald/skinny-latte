/*
 * This source has been modified. The original source applied this license:
 *
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jameswald.skinnylatte.common.base;

import static com.jameswald.skinnylatte.common.base.Preconditions.checkNotNull;

public final class Objects {
  private Objects() {}

  public static ToStringHelper toStringHelper(Object self) {
    return new ToStringHelper(simpleName(self.getClass()));
  }

  public static ToStringHelper toStringHelper(Class<?> clazz) {
    return new ToStringHelper(simpleName(clazz));
  }

  public static ToStringHelper toStringHelper(String className) {
    return new ToStringHelper(className);
  }

  private static String simpleName(Class<?> clazz) {
    String name = clazz.getName();

    name = name.replaceAll("\\$[0-9]+", "\\$");

    int start = name.lastIndexOf('$');

    if (start == -1) {
      start = name.lastIndexOf('.');
    }
    return name.substring(start + 1);
  }

  public static final class ToStringHelper {
    private final String className;
    private ValueHolder holderHead = new ValueHolder();
    private ValueHolder holderTail = holderHead;
    private boolean omitNullValues = false;

    private ToStringHelper(String className) {
      this.className = checkNotNull(className);
    }

    public ToStringHelper add(String name, Object value) {
      return addHolder(name, value);
    }

    public ToStringHelper add(String name, boolean value) {
      return addHolder(name, String.valueOf(value));
    }

    public ToStringHelper add(String name, char value) {
      return addHolder(name, String.valueOf(value));
    }

    public ToStringHelper add(String name, double value) {
      return addHolder(name, String.valueOf(value));
    }

    public ToStringHelper add(String name, float value) {
      return addHolder(name, String.valueOf(value));
    }

    public ToStringHelper add(String name, int value) {
      return addHolder(name, String.valueOf(value));
    }

    public ToStringHelper add(String name, long value) {
      return addHolder(name, String.valueOf(value));
    }

    @Override public String toString() {
      boolean omitNullValuesSnapshot = omitNullValues;
      String nextSeparator = "";
      StringBuilder builder = new StringBuilder(32).append(className)
          .append('{');
      for (ValueHolder valueHolder = holderHead.next; valueHolder != null;
          valueHolder = valueHolder.next) {
        if (!omitNullValuesSnapshot || valueHolder.value != null) {
          builder.append(nextSeparator);
          nextSeparator = ", ";

          if (valueHolder.name != null) {
            builder.append(valueHolder.name).append('=');
          }
          builder.append(valueHolder.value);
        }
      }
      return builder.append('}').toString();
    }

    private ValueHolder addHolder() {
      ValueHolder valueHolder = new ValueHolder();
      holderTail = holderTail.next = valueHolder;
      return valueHolder;
    }

    private ToStringHelper addHolder(String name, Object value) {
      ValueHolder valueHolder = addHolder();
      valueHolder.value = value;
      valueHolder.name = checkNotNull(name);
      return this;
    }

    private static final class ValueHolder {
      String name;
      Object value;
      ValueHolder next;
    }
  }
}
