package fix

import metaconfig.{ConfDecoder, Configured}
import metaconfig.generic.Surface
import scalafix.v1._

import scala.meta._

object ForbiddenSymbolConfig {
  def default: ForbiddenSymbolConfig = ForbiddenSymbolConfig()
  implicit val surface: Surface[ForbiddenSymbolConfig] = metaconfig.generic.deriveSurface[ForbiddenSymbolConfig]
  implicit val decoder: ConfDecoder[ForbiddenSymbolConfig] = metaconfig.generic.deriveDecoder(default)
}

case class ForbiddenSymbolConfig(
  symbols: List[String] = List.empty
)

class ForbiddenSymbol(config: ForbiddenSymbolConfig) extends SemanticRule("ForbiddenSymbol") {

  def this() = this(ForbiddenSymbolConfig())

  override def withConfiguration(config: Configuration): Configured[Rule] =
    config.conf
      .getOrElse("ForbiddenSymbol")(this.config)
      .map { newConfig => new ForbiddenSymbol(newConfig) }

  case class Forbidden(name: Name)(implicit doc: SemanticDocument) extends Diagnostic {
    val position: Position = name.pos

    override def message = s"${name.symbol.normalized.value.stripSuffix(".")} is forbidden according to the ForbiddenSymbol rule."
  }

  private[this] val forbiddenSymbolSymbolMatchers = config.symbols.map { c => SymbolMatcher.normalized(c) }

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t: Name if forbiddenSymbolSymbolMatchers.exists(_.matches(t)) =>
        Patch.lint(Forbidden(t))
    }.asPatch
  }
}
