package protein

import com.intellij.ui.wizard.WizardModel
import protein.steps.ModuleSelectorStep
import protein.steps.PackageInfoStep

class AddComponentWizardModel(title: String) : WizardModel(title) {

  init {
    add(ModuleSelectorStep())
    add(PackageInfoStep())
  }
}
