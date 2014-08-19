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

package com.jameswald.skinnylatte.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class ListenableFutureTask<V> extends FutureTask<V>
    implements ListenableFuture<V> {
  private final ExecutionList executionList = new ExecutionList();

  public static <V> ListenableFutureTask<V> create(Callable<V> callable) {
    return new ListenableFutureTask<V>(callable);
  }

  public static <V> ListenableFutureTask<V> create(
      Runnable runnable, V result) {
    return new ListenableFutureTask<V>(runnable, result);
  }

  ListenableFutureTask(Callable<V> callable) {
    super(callable);
  }

  ListenableFutureTask(Runnable runnable, V result) {
    super(runnable, result);
  }

  @Override
  public void addListener(Runnable listener, Executor exec) {
    executionList.add(listener, exec);
  }

  @Override
  protected void done() {
    executionList.execute();
  }
}
