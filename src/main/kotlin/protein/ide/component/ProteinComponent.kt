package protein.ide.component

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.extensions.PluginId

class ProteinComponent : ApplicationComponent {
  var updated: Boolean = false

  override fun initComponent() {
    updated = getPlugin()?.version != ProteinSettings.instance.version
    if (updated) {
      ProteinSettings.instance.version = getPlugin()?.version
    }
  }

  companion object {
    val instance: ProteinComponent
      get() = ApplicationManager.getApplication().getComponent(ProteinComponent::class.java)

    private fun getPlugin(): IdeaPluginDescriptor? =
        PluginManager.getPlugin(PluginId.getId("com.schibsted.protein"))
  }
}
