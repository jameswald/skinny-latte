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

package com.jameswald.skinnylatte.common.collect;

import static com.jameswald.skinnylatte.common.base.Preconditions.checkNotNull;

import com.jameswald.skinnylatte.common.base.Predicate;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public final class Range<C extends Comparable> implements Predicate<C>, Serializable {

  private static final Range<Comparable> ALL =
    new Range<Comparable>(Cut.belowAll(), Cut.aboveAll());

  @SuppressWarnings("unchecked")
  public static <C extends Comparable<?>> Range<C> all() {
    return (Range) ALL;
  }

  static <C extends Comparable<?>> Range<C> create(
      Cut<C> lowerBound, Cut<C> upperBound) {
    return new Range<C>(lowerBound, upperBound);
  }

  public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
    return create(Cut.belowValue(lower), Cut.aboveValue(upper));
  }

  final Cut<C> lowerBound;
  final Cut<C> upperBound;

  private Range(Cut<C> lowerBound, Cut<C> upperBound) {
    if (lowerBound.compareTo(upperBound) > 0 || lowerBound == Cut.<C>aboveAll()
        || upperBound == Cut.<C>belowAll()) {
      throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
    }
    this.lowerBound = checkNotNull(lowerBound);
    this.upperBound = checkNotNull(upperBound);
  }

  public boolean contains(C value) {
    checkNotNull(value);
    return lowerBound.isLessThan(value) && !upperBound.isLessThan(value);
  }

  @Deprecated
  @Override
  public boolean apply(C input) {
    return contains(input);
  }

  @Override public boolean equals(Object object) {
    if (object instanceof Range) {
      Range<?> other = (Range<?>) object;
      return lowerBound.equals(other.lowerBound)
          && upperBound.equals(other.upperBound);
    }
    return false;
  }

  @Override public int hashCode() {
    return lowerBound.hashCode() * 31 + upperBound.hashCode();
  }

  @Override public String toString() {
    return toString(lowerBound, upperBound);
  }

  private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
    StringBuilder sb = new StringBuilder(16);
    lowerBound.describeAsLowerBound(sb);
    sb.append('\u2025');
    upperBound.describeAsUpperBound(sb);
    return sb.toString();
  }

  Object readResolve() {
    if (this.equals(ALL)) {
      return all();
    } else {
      return this;
    }
  }

  static int compareOrThrow(Comparable left, Comparable right) {
    return left.compareTo(right);
  }

  private static final long serialVersionUID = 0;
}
