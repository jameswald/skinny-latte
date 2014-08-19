/*
 * This source has been modified. The original source applied this license:
 *
 * Copyright (C) 2011 The Guava Authors
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

final class Absent<T> extends Optional<T> {
  static final Absent<Object> INSTANCE = new Absent<Object>();

  @SuppressWarnings("unchecked") // implementation is "fully variant"
  static <T> Optional<T> withType() {
    return (Optional<T>) INSTANCE;
  }

  private Absent() {}

  @Override public boolean isPresent() {
    return false;
  }

  @Override public T get() {
    throw new IllegalStateException("Optional.get() cannot be called on an absent value");
  }

  @Override public T or(T defaultValue) {
    return checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
  }

  @SuppressWarnings("unchecked") // safe covariant cast
  @Override public Optional<T> or(Optional<? extends T> secondChoice) {
    return (Optional<T>) checkNotNull(secondChoice);
  }

  @Override public T or(Supplier<? extends T> supplier) {
    return checkNotNull(supplier.get(),
        "use Optional.orNull() instead of a Supplier that returns null");
  }

  @Override public boolean equals(Object object) {
    return object == this;
  }

  @Override public int hashCode() {
    return 0x598df91c;
  }

  @Override public String toString() {
    return "Optional.absent()";
  }

  private Object readResolve() {
    return INSTANCE;
  }

  private static final long serialVersionUID = 0;
}
