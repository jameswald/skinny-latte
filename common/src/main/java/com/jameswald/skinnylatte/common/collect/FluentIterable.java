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

import java.util.Iterator;

public abstract class FluentIterable<E> implements Iterable<E> {
  private final Iterable<E> iterable;

  protected FluentIterable() {
    this.iterable = this;
  }

  FluentIterable(Iterable<E> iterable) {
    this.iterable = checkNotNull(iterable);
  }

  public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
    return (iterable instanceof FluentIterable) ? (FluentIterable<E>) iterable
        : new FluentIterable<E>(iterable) {
          @Override
          public Iterator<E> iterator() {
            return iterable.iterator();
          }
        };
  }

  @Override
  public String toString() {
    return Iterables.toString(iterable);
  }
}
