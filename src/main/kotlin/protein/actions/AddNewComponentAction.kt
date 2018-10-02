package protein.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import protein.AddComponentWizard

class AddNewComponentAction : AnAction() {

  override fun actionPerformed(event: AnActionEvent) {
    AddComponentWizard.run()
  }
}
