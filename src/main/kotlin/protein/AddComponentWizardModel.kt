package protein

import com.intellij.ui.wizard.WizardModel
import protein.steps.PackageInfoStep
import protein.steps.ModuleSelectorStep

class AddComponentWizardModel(title: String) : WizardModel(title) {

  init {
    add(ModuleSelectorStep())
    add(PackageInfoStep())
  }
}
