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

package com.jameswald.skinnylatte.common.util.concurrent;

import com.jameswald.skinnylatte.common.base.Preconditions;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExecutionList {
  static final Logger log = Logger.getLogger(ExecutionList.class.getName());

  private RunnableExecutorPair runnables;
  private boolean executed;

  public ExecutionList() {}

  public void add(Runnable runnable, Executor executor) {
    Preconditions.checkNotNull(runnable, "Runnable was null.");
    Preconditions.checkNotNull(executor, "Executor was null.");

    synchronized (this) {
      if (!executed) {
        runnables = new RunnableExecutorPair(runnable, executor, runnables);
        return;
      }
    }
    executeListener(runnable, executor);
  }

  public void execute() {
    RunnableExecutorPair list;
    synchronized (this) {
      if (executed) {
        return;
      }
      executed = true;
      list = runnables;
      runnables = null;
    }

    RunnableExecutorPair reversedList = null;
    while (list != null) {
      RunnableExecutorPair tmp = list;
      list = list.next;
      tmp.next = reversedList;
      reversedList = tmp;
    }
    while (reversedList != null) {
      executeListener(reversedList.runnable, reversedList.executor);
      reversedList = reversedList.next;
    }
  }

  private static void executeListener(Runnable runnable, Executor executor) {
    try {
      executor.execute(runnable);
    } catch (RuntimeException e) {
      log.log(Level.SEVERE, "RuntimeException while executing runnable "
          + runnable + " with executor " + executor, e);
    }
  }

  private static final class RunnableExecutorPair {
    final Runnable runnable;
    final Executor executor;
    RunnableExecutorPair next;

    RunnableExecutorPair(Runnable runnable, Executor executor, RunnableExecutorPair next) {
      this.runnable = runnable;
      this.executor = executor;
      this.next = next;
    }
  }
}
