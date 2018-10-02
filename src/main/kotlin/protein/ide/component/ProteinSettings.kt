package protein.ide.component

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager.getService
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil.copyBean
import org.jetbrains.annotations.Nullable

@State(name = "ProteinSettings", storages = [(Storage("\$APP_CONFIG$/protein.xml"))])
class ProteinSettings : PersistentStateComponent<ProteinSettings> {
  var version: String? = null

  @Nullable
  override fun getState() = this

  override fun loadState(state: ProteinSettings) {
    copyBean(state, this)
  }

  companion object {
    val instance: ProteinSettings
      get() = getService(ProteinSettings::class.java)
  }
}
