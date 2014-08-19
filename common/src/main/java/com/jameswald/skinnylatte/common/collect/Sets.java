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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public final class Sets {
  private Sets() {}

  public static <E> HashSet<E> newHashSet() {
    return new HashSet<E>();
  }

  public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
    return (elements instanceof Collection)
        ? new HashSet<E>(Collections2.cast(elements))
        : newHashSet(elements.iterator());
  }

  public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
    HashSet<E> set = newHashSet();
    Iterators.addAll(set, elements);
    return set;
  }
}
