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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class MoreExecutors {
  private MoreExecutors() {}

  public static ListeningExecutorService sameThreadExecutor() {
    return new SameThreadExecutorService();
  }

  private static class SameThreadExecutorService
      extends AbstractListeningExecutorService {
    private final Lock lock = new ReentrantLock();

    private final Condition termination = lock.newCondition();

    private int runningTasks = 0;
    private boolean shutdown = false;

    @Override
    public void execute(Runnable command) {
      startTask();
      try {
        command.run();
      } finally {
        endTask();
      }
    }

    @Override
    public boolean isShutdown() {
      lock.lock();
      try {
        return shutdown;
      } finally {
        lock.unlock();
      }
    }

    @Override
    public void shutdown() {
      lock.lock();
      try {
        shutdown = true;
      } finally {
        lock.unlock();
      }
    }

    @Override
    public List<Runnable> shutdownNow() {
      shutdown();
      return Collections.emptyList();
    }

    @Override
    public boolean isTerminated() {
      lock.lock();
      try {
        return shutdown && runningTasks == 0;
      } finally {
        lock.unlock();
      }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      lock.lock();
      try {
        for (;;) {
          if (isTerminated()) {
            return true;
          } else if (nanos <= 0) {
            return false;
          } else {
            nanos = termination.awaitNanos(nanos);
          }
        }
      } finally {
        lock.unlock();
      }
    }

    private void startTask() {
      lock.lock();
      try {
        if (isShutdown()) {
          throw new RejectedExecutionException("Executor already shutdown");
        }
        runningTasks++;
      } finally {
        lock.unlock();
      }
    }

    private void endTask() {
      lock.lock();
      try {
        runningTasks--;
        if (isTerminated()) {
          termination.signalAll();
        }
      } finally {
        lock.unlock();
      }
    }
  }
}
