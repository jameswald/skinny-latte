Skinny Latte
============

A shot of Android [Espresso][espresso] that won't runneth over your cup.

Libraries should be small and focused, especially when the library is intended for resource
constrained devices. Android Test Kit's Espresso relies on relatively large dependencies such as
Guava which consumes nearly 30% of the total number of methods supported by Dalvik.

Skinny Latte removes those dependencies to avoid Dalvik's >64K method limit, without the need to
pre-process dependencies using ProGuard. This reduces the amount of time spent waiting for dex
during dev-test cycles.  The Espresso API is unchanged except for dependency declarations that have
been replaced with small local projects.

```groovy
androidTestCompile 'com.jameswald.skinnylatte:espresso:1.1-r1'
androidTestCompile 'com.jameswald.skinnylatte:espresso-support-v4:1.1-r1'
```

Configure the build to use Espresso's custom test runner:
```groovy
defaultConfig {
  testInstrumentationRunner 'com.google.android.apps.common.testing.testrunner.GoogleInstrumentationTestRunner'
}
```

The Espresso classes have only been modified to replace dependencies (see the [diff][diff]). No
features have been added to the API. Only changes that remove or replace dependencies will be
accepted.

Notes
-----

This is currently based on Jake Wharton's [Double Espresso][double-espresso], a pure Gradle port of
Espresso.

License
-------

    Copyright 2014 James Wald

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [espresso]: https://code.google.com/p/android-test-kit/
 [double-espresso]: https://github.com/JakeWharton/double-espresso
 [diff]: https://github.com/jameswald/skinny-latte/compare/gradle...skinny
