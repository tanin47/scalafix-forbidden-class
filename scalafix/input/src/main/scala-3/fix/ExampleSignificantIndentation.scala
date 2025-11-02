/*
rule = ForbiddenSymbol
ForbiddenSymbol.symbols = [
  "java.util",
  "java.time.Instant",
  "java.math.BigDecimal.valueOf",
  "java.math.BigDecimal.ONE",
]
*/
package fix

import java.time.Instant/* assert: ForbiddenSymbol
                 ^^^^^^^
java.time.Instant is forbidden according to the ForbiddenSymbol rule.
*/
import java.util.Date/* assert: ForbiddenSymbol
            ^^^^
java.util is forbidden according to the ForbiddenSymbol rule.
*/

object ExampleSignificantIndentation:
  val now = Instant.now()/* assert: ForbiddenSymbol
            ^^^^^^^
  java.time.Instant is forbidden according to the ForbiddenSymbol rule.
  */
  val s = Date.from(now)
  java.math.BigDecimal.valueOf(123)/* assert: ForbiddenSymbol
                       ^^^^^^^
  java.math.BigDecimal.valueOf is forbidden according to the ForbiddenSymbol rule.
  */
  println(java.math.BigDecimal.ONE)/* assert: ForbiddenSymbol
                               ^^^
  java.math.BigDecimal.ONE is forbidden according to the ForbiddenSymbol rule.
  */
