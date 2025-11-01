/*
rule = ForbiddenSymbol
ForbiddenSymbol.symbols = [
  "java.time",
  "java.time.Instant",
  "java.time.Instant.now",
  "java.time.temporal.ChronoUnit.YEARS",
]
*/
package fix

import java.time.Instant

object ExampleSignificantIndentation:
  val now = Instant.now()
  println(java.time.temporal.ChronoUnit.YEARS)
