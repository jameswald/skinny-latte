/*
 * This source has been modified. The original source applied this license:
 *
 * Copyright (C) 2008 The Guava Authors
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

import java.io.IOException;
import java.util.Iterator;

public class Joiner {
  public static Joiner on(String separator) {
    return new Joiner(separator);
  }

  public static Joiner on(char separator) {
    return new Joiner(String.valueOf(separator));
  }

  private final String separator;

  private Joiner(String separator) {
    this.separator = checkNotNull(separator);
  }

  private Joiner(Joiner prototype) {
    this.separator = prototype.separator;
  }

  public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
    checkNotNull(appendable);
    if (parts.hasNext()) {
      appendable.append(toString(parts.next()));
      while (parts.hasNext()) {
        appendable.append(separator);
        appendable.append(toString(parts.next()));
      }
    }
    return appendable;
  }

  public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
    return appendTo(builder, parts.iterator());
  }

  public final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts) {
    try {
      appendTo((Appendable) builder, parts);
    } catch (IOException impossible) {
      throw new AssertionError(impossible);
    }
    return builder;
  }

  public final String join(Iterable<?> parts) {
    return join(parts.iterator());
  }

  public final String join(Iterator<?> parts) {
    return appendTo(new StringBuilder(), parts).toString();
  }

  public Joiner useForNull(final String nullText) {
    checkNotNull(nullText);
    return new Joiner(this) {
      @Override CharSequence toString(Object part) {
        return (part == null) ? nullText : Joiner.this.toString(part);
      }

      @Override public Joiner useForNull(String nullText) {
        throw new UnsupportedOperationException("already specified useForNull");
      }
    };
  }

  CharSequence toString(Object part) {
    checkNotNull(part);  // checkNotNull for GWT (do not optimize).
    return (part instanceof CharSequence) ? (CharSequence) part : part.toString();
  }
}
