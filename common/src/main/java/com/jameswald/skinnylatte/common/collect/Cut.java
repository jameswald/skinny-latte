/*
 * This source has been modified. The original source applied this license:
 *
 * Copyright (C) 2009 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jameswald.skinnylatte.common.collect;

import static com.jameswald.skinnylatte.common.base.Preconditions.checkNotNull;

import com.jameswald.skinnylatte.common.primitives.Booleans;

import java.io.Serializable;

abstract class Cut<C extends Comparable> implements Comparable<Cut<C>>, Serializable {
  final C endpoint;

  Cut(C endpoint) {
    this.endpoint = endpoint;
  }

  abstract boolean isLessThan(C value);

  abstract void describeAsLowerBound(StringBuilder sb);
  abstract void describeAsUpperBound(StringBuilder sb);

  @Override
  public int compareTo(Cut<C> that) {
    if (that == belowAll()) {
      return 1;
    }
    if (that == aboveAll()) {
      return -1;
    }
    int result = Range.compareOrThrow(endpoint, that.endpoint);
    if (result != 0) {
      return result;
    }
    // same value. below comes before above
    return Booleans.compare(
        this instanceof AboveValue, that instanceof AboveValue);
  }

  @SuppressWarnings("unchecked") // catching CCE
  @Override public boolean equals(Object obj) {
    if (obj instanceof Cut) {
      // It might not really be a Cut<C>, but we'll catch a CCE if it's not
      Cut<C> that = (Cut<C>) obj;
      try {
        int compareResult = compareTo(that);
        return compareResult == 0;
      } catch (ClassCastException ignored) {
      }
    }
    return false;
  }

  /*
   * The implementation neither produces nor consumes any non-null instance of type C, so
   * casting the type parameter is safe.
   */
  @SuppressWarnings("unchecked")
  static <C extends Comparable> Cut<C> belowAll() {
    return (Cut<C>) BelowAll.INSTANCE;
  }

  private static final long serialVersionUID = 0;

  private static final class BelowAll extends Cut<Comparable<?>> {
    private static final BelowAll INSTANCE = new BelowAll();

    private BelowAll() {
      super(null);
    }
    @Override boolean isLessThan(Comparable<?> value) {
      return true;
    }
    @Override void describeAsLowerBound(StringBuilder sb) {
      sb.append("(-\u221e");
    }
    @Override void describeAsUpperBound(StringBuilder sb) {
      throw new AssertionError();
    }
    @Override public int compareTo(Cut<Comparable<?>> o) {
      return (o == this) ? 0 : -1;
    }
    @Override public String toString() {
      return "-\u221e";
    }
    private Object readResolve() {
      return INSTANCE;
    }
    private static final long serialVersionUID = 0;
  }

  /*
   * The implementation neither produces nor consumes any non-null instance of
   * type C, so casting the type parameter is safe.
   */
  @SuppressWarnings("unchecked")
  static <C extends Comparable> Cut<C> aboveAll() {
    return (Cut<C>) AboveAll.INSTANCE;
  }

  private static final class AboveAll extends Cut<Comparable<?>> {
    private static final AboveAll INSTANCE = new AboveAll();

    private AboveAll() {
      super(null);
    }
    @Override boolean isLessThan(Comparable<?> value) {
      return false;
    }
    @Override void describeAsLowerBound(StringBuilder sb) {
      throw new AssertionError();
    }
    @Override void describeAsUpperBound(StringBuilder sb) {
      sb.append("+\u221e)");
    }
    @Override public int compareTo(Cut<Comparable<?>> o) {
      return (o == this) ? 0 : 1;
    }
    @Override public String toString() {
      return "+\u221e";
    }
    private Object readResolve() {
      return INSTANCE;
    }
    private static final long serialVersionUID = 0;
  }

  static <C extends Comparable> Cut<C> belowValue(C endpoint) {
    return new BelowValue<C>(endpoint);
  }

  private static final class BelowValue<C extends Comparable> extends Cut<C> {
    BelowValue(C endpoint) {
      super(checkNotNull(endpoint));
    }

    @Override boolean isLessThan(C value) {
      return Range.compareOrThrow(endpoint, value) <= 0;
    }
    @Override void describeAsLowerBound(StringBuilder sb) {
      sb.append('[').append(endpoint);
    }
    @Override void describeAsUpperBound(StringBuilder sb) {
      sb.append(endpoint).append(')');
    }
    @Override public int hashCode() {
      return endpoint.hashCode();
    }
    @Override public String toString() {
      return "\\" + endpoint + "/";
    }
    private static final long serialVersionUID = 0;
  }

  static <C extends Comparable> Cut<C> aboveValue(C endpoint) {
    return new AboveValue<C>(endpoint);
  }

  private static final class AboveValue<C extends Comparable> extends Cut<C> {
    AboveValue(C endpoint) {
      super(checkNotNull(endpoint));
    }

    @Override boolean isLessThan(C value) {
      return Range.compareOrThrow(endpoint, value) < 0;
    }
    @Override void describeAsLowerBound(StringBuilder sb) {
      sb.append('(').append(endpoint);
    }
    @Override void describeAsUpperBound(StringBuilder sb) {
      sb.append(endpoint).append(']');
    }
    @Override public int hashCode() {
      return ~endpoint.hashCode();
    }
    @Override public String toString() {
      return "/" + endpoint + "\\";
    }
    private static final long serialVersionUID = 0;
  }
}
