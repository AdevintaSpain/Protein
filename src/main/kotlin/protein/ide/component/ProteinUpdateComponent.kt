package protein.ide.component

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project

class ProteinUpdateComponent(project: Project) : AbstractProjectComponent(project) {

  private val applicationComponent = ProteinComponent.instance

  override fun projectOpened() {
    if (applicationComponent.updated) {
      showUpdate(myProject)
      applicationComponent.updated = false
    }
  }

  companion object {
    private fun getPlugin(): IdeaPluginDescriptor? =
        PluginManager.getPlugin(PluginId.getId("com.schibsted.protein"))

    private val version = getPlugin()?.version
    private var channel = "com.schibsted.protein"
    private const val updateContent = """
    <br/>
    Thank you for downloading <b>Protein</b>!<br>
    If you find this plugin helpful,
    <b><a href="https://github.com/SchibstedSpain/Protein">
    Please give us a star on Github</a></b><br/>
    If you find any issue, <b><a href="https://github.com/SchibstedSpain/Protein/issues/new/choose">Feel free to raise a issue</a></b><br/>
    See <b><a href="https://github.com/SchibstedSpain/Protein/blob/master/CHANGELOG.md">Changelog</a></b>
    for more details.
    """

    private fun showUpdate(project: Project) {
      show(
          project,
          "Protein updated to $version",
          updateContent,
          channel + "_UPDATE",
          NotificationType.INFORMATION,
          NotificationListener.URL_OPENING_LISTENER
      )
    }
  }
}
