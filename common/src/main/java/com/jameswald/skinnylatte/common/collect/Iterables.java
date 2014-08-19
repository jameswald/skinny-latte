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

package com.jameswald.skinnylatte.common.collect;

import static com.jameswald.skinnylatte.common.base.Preconditions.checkNotNull;

import com.jameswald.skinnylatte.common.base.Function;
import com.jameswald.skinnylatte.common.base.Predicate;

import java.util.Collection;
import java.util.Iterator;

public final class Iterables {
  private Iterables() {}

  public static String toString(Iterable<?> iterable) {
    return Iterators.toString(iterable.iterator());
  }

  public static <T> T getOnlyElement(Iterable<T> iterable) {
    return Iterators.getOnlyElement(iterable.iterator());
  }

  public static <T> T getOnlyElement(
      Iterable<? extends T> iterable, T defaultValue) {
    return Iterators.getOnlyElement(iterable.iterator(), defaultValue);
  }

  public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
    Collection<? extends T> collection = toCollection(iterable);
    T[] array = ObjectArrays.newArray(type, collection.size());
    return collection.toArray(array);
  }

  private static <E> Collection<E> toCollection(Iterable<E> iterable) {
    return (iterable instanceof Collection)
        ? (Collection<E>) iterable
        : Lists.newArrayList(iterable.iterator());
  }

  public static <T> Iterable<T> filter(
      final Iterable<T> unfiltered, final Predicate<? super T> predicate) {
    checkNotNull(unfiltered);
    checkNotNull(predicate);
    return new FluentIterable<T>() {
        @Override
        public Iterator<T> iterator() {
          return Iterators.filter(unfiltered.iterator(), predicate);
        }
    };
  }

  public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable,
      final Function<? super F, ? extends T> function) {
    checkNotNull(fromIterable);
    checkNotNull(function);
    return new FluentIterable<T>() {
      @Override
      public Iterator<T> iterator() {
        return Iterators.transform(fromIterable.iterator(), function);
      }
    };
  }
}
