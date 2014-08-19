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

package com.jameswald.skinnylatte.common.base;

import static com.jameswald.skinnylatte.common.base.Preconditions.checkNotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class Throwables {
  private Throwables() {}

  public static <X extends Throwable> void propagateIfInstanceOf(
      Throwable throwable, Class<X> declaredType) throws X {
    if (throwable != null && declaredType.isInstance(throwable)) {
      throw declaredType.cast(throwable);
    }
  }

  public static void propagateIfPossible(Throwable throwable) {
    propagateIfInstanceOf(throwable, Error.class);
    propagateIfInstanceOf(throwable, RuntimeException.class);
  }

  public static RuntimeException propagate(Throwable throwable) {
    propagateIfPossible(checkNotNull(throwable));
    throw new RuntimeException(throwable);
  }

  public static String getStackTraceAsString(Throwable throwable) {
    StringWriter stringWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }
}
