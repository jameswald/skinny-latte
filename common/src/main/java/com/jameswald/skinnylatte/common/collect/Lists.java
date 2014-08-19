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

import com.jameswald.skinnylatte.common.primitives.Ints;

import static com.jameswald.skinnylatte.common.base.Preconditions.checkNotNull;
import static com.jameswald.skinnylatte.common.collect.CollectPreconditions.checkNonnegative;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public final class Lists {
  private Lists() {}

  public static <E> ArrayList<E> newArrayList() {
    return new ArrayList<E>();
  }

  public static <E> ArrayList<E> newArrayList(E... elements) {
    checkNotNull(elements);
    int capacity = computeArrayListCapacity(elements.length);
    ArrayList<E> list = new ArrayList<E>(capacity);
    Collections.addAll(list, elements);
    return list;
  }

  static int computeArrayListCapacity(int arraySize) {
    checkNonnegative(arraySize, "arraySize");
    return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
  }

  public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
    checkNotNull(elements); // for GWT
    // Let ArrayList's sizing logic work, if possible
    return (elements instanceof Collection)
        ? new ArrayList<E>(Collections2.cast(elements))
        : newArrayList(elements.iterator());
  }

  public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
    ArrayList<E> list = newArrayList();
    Iterators.addAll(list, elements);
    return list;
  }

  public static <E> LinkedList<E> newLinkedList() {
    return new LinkedList<E>();
  }
}
