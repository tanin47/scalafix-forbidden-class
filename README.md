Scalafix Forbidden Symbol
=========================

[![Sonatype Central](https://maven-badges.sml.io/sonatype-central/io.github.tanin47/scalafix-forbidden-symbol_2.13/badge.png)](https://central.sonatype.com/artifact/io.github.tanin47/scalafix-forbidden-symbol_2.13)

The Scalafix rule for forbidding specified packages, classes, methods, and enums to be used in the codebase.

In fact, it works with any symbol that is not a literal.

This was originally built as a part of [PlayFast](https://github.com/tanin47/playfast), an opinionated productive-devx production-ready PlayFramework template. It is used to forbid `java.time.Instant` in favor of `framework.Instant` (our own Instant) because `java.time.Instant` isn't mockable in PlayFramework's test.

How to use
-----------

1. Add the Scalafix ForbiddenSymbol jar to your `build.sbt`:

```
ThisBuild / scalafixDependencies += "io.github.tanin47" %% "scalafix-forbidden-symbol" % "1.0.0"
```

2. Add the rule to your `.scalafix.conf`:

```
rules = [
  ForbiddenSymbol
]
```

3. Specify the forbidden classes as shown below:

```
ForbiddenSymbol.symbols = [
  "java.time", // Package
  "java.time.Instant", // Class
  "java.time.Instant.now", // Method
  "java.time.temporal.ChronoUnit.YEARS" // Enum
]
```

4. It would be good to test it out by using a forbidden class and run `sbt scalafixAll`. You should see an error.

5. If you would like to disable this Scalafix rule on a certain line in your codebase, you can use `// scalafix:off ForbiddenSymbol`.

How to develop
---------------

Run `sbt ~tests/test`


How to publish
---------------

1. Publish locally with: `sbt publishLocal`.
2. Test that the local version works. We can test with https://github.com/tanin47/playfast
3. Build the publishable JAR by running `sbt clean publishSigned`
4. Upload to Maven Central (Sonatype) by running `sbt sonaUpload`
