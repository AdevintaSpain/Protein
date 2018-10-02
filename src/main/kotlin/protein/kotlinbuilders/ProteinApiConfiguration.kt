package protein.kotlinbuilders

data class ProteinApiConfiguration(
  override val serviceEndpoint: String,
  override val swaggerUrl: String,
  override val packageName: String,
  override val componentName: String,
  override val moduleName: String,
  override val swaggerFile: String
) : ProteinConfiguration

interface ProteinConfiguration {
  val serviceEndpoint: String
  val swaggerUrl: String
  val packageName: String
  val componentName: String
  val moduleName: String
  val swaggerFile: String
}
