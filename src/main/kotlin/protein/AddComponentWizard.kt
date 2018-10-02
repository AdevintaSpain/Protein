package protein

import com.intellij.ui.wizard.WizardDialog

import javax.swing.SwingUtilities
import java.awt.Dimension

class AddComponentWizard : WizardDialog<AddComponentWizardModel>(
    true, true, AddComponentWizardModel("Protein - Add new Component")) {

  override fun getWindowPreferredSize(): Dimension {
    return Dimension(WIDTH, HEIGHT)
  }

  companion object {
    const val WIDTH = 1024
    const val HEIGHT = 500

    fun run() {
      AddComponentWizard().show()
    }

    @JvmStatic
    fun main(args: Array<String>) {
      SwingUtilities.invokeLater { AddComponentWizard.run() }
    }
  }
}
