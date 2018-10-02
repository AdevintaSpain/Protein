package protein.steps;

import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import org.jetbrains.annotations.NotNull;
import protein.AddComponentWizardModel;
import protein.common.Settings;
import protein.common.SettingsManager;

import javax.swing.*;

import java.util.List;

import static protein.common.StorageUtils.getFoldersList;

public class ModuleSelectorStep extends WizardStep<AddComponentWizardModel> {

    private JLabel header;
    private JPanel rootPanel;
    private JList modulesList;
    private JList savedSettingsList;
    private JPanel headerPanel;

    public ModuleSelectorStep() {
    }

    @Override
    public JComponent prepare(WizardNavigationState wizardNavigationState) {
        rootPanel.revalidate();
        modulesList.setModel(getFoldersList());
        savedSettingsList.setModel(getSettingsListModel());
        return rootPanel;
    }

    @NotNull
    private DefaultListModel getSettingsListModel() {
        DefaultListModel settingsListModel = new DefaultListModel();
        SettingsManager.getInstance().load();
        List<Settings> settingsList = SettingsManager.getInstance().getSettingsList();
        for (Settings settings : settingsList) {
            settingsListModel.addElement(settings.getComponentName());
        }
        return settingsListModel;
    }

    @Override
    public WizardStep onNext(AddComponentWizardModel model) {
        if (!savedSettingsList.isSelectionEmpty()) {
            Settings settings = SettingsManager.getInstance().getSettingsList().get(savedSettingsList.getSelectedIndex());
            Settings.getInstance().setComponentName(settings.getComponentName());
            Settings.getInstance().setDomainName(settings.getDomainName());
            Settings.getInstance().setSwaggerUrl(settings.getSwaggerUrl());
            Settings.getInstance().setServiceEndpoint(settings.getServiceEndpoint());
            Settings.getInstance().setModuleName(settings.getModuleName());
        }
        if (!modulesList.isSelectionEmpty()) {
            Settings.getInstance().setModuleName((String) modulesList.getSelectedValue());
        }
        return super.onNext(model);
    }
}
