package protein.steps;

import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.schibsted.spain.retroswagger.lib.RetroswaggerApiBuilder;
import com.schibsted.spain.retroswagger.lib.RetroswaggerApiConfiguration;
import com.schibsted.spain.retroswagger.lib.RetroswaggerErrorTracking;
import com.squareup.kotlinpoet.TypeSpec;
import protein.AddComponentWizardModel;
import protein.common.Settings;
import protein.common.SettingsManager;
import protein.common.StorageUtils;
import protein.tracking.BugsnagErrorTracking;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComponent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static protein.common.StorageUtils.toFirstUpperCase;

public class PackageInfoStep extends WizardStep<AddComponentWizardModel> {
    private JPanel rootPanel;
    private JPanel headerPanel;
    private JPanel swaggerPanel;
    private JPanel agentPanel;
    private JLabel header;
    private JTextField swaggerUrlTextField;
    private JTextField serviceEndPointTextField;
    private JLabel packageNameLabel;
    private JTextField componentNameTextField;
    private JTextField domainTextField;
    private RetroswaggerErrorTracking errorTracking = new BugsnagErrorTracking();

    @Override
    public JComponent prepare(WizardNavigationState wizardNavigationState) {
        rootPanel.revalidate();

        if (Settings.getInstance().getComponentName() != null && !"".equals(Settings.getInstance().getComponentName())) {
            componentNameTextField.setText(Settings.getInstance().getComponentName());
            domainTextField.setText(Settings.getInstance().getDomainName());
            swaggerUrlTextField.setText(Settings.getInstance().getSwaggerUrl());
            serviceEndPointTextField.setText(Settings.getInstance().getServiceEndpoint());
        }

        setKeyListeners();
        setWizardFinishButtonProperties(wizardNavigationState);

        return rootPanel;
    }

    private void setWizardFinishButtonProperties(WizardNavigationState wizardNavigationState) {
        swaggerUrlTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                checkIfCanEnableFinishButton(wizardNavigationState);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                checkIfCanEnableFinishButton(wizardNavigationState);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                checkIfCanEnableFinishButton(wizardNavigationState);
            }
        });
        wizardNavigationState.FINISH.setName("GENERATE");
        if (!canFinish()) {
            wizardNavigationState.FINISH.setEnabled(false);
        }
    }

    private void setKeyListeners() {
        KeyListener textChangeKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                packageNameLabel.setText(domainTextField.getText() + "." + componentNameTextField.getText());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                packageNameLabel.setText(domainTextField.getText() + "." + componentNameTextField.getText());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                packageNameLabel.setText(domainTextField.getText() + "." + componentNameTextField.getText());
            }
        };
        componentNameTextField.addKeyListener(textChangeKeyListener);
        domainTextField.addKeyListener(textChangeKeyListener);
    }

    private void checkIfCanEnableFinishButton(WizardNavigationState wizardNavigationState) {
        if (canFinish()) {
            wizardNavigationState.FINISH.setEnabled(true);
        }
    }

    @Override
    public boolean onFinish() {
        if (canFinish()) {
            updateSettingsValues();
            saveSettings();

            buildKotlinApi();
        }
        return super.onFinish();
    }

    private boolean canFinish() {
        return !"".equals(this.componentNameTextField.getText())
                && !"".equals(this.domainTextField.getText())
                && !"".equals(this.swaggerUrlTextField.getText());
    }

    private void saveSettings() {
        SettingsManager.getInstance().add(Settings.getInstance());
        SettingsManager.getInstance().save();
    }

    private void updateSettingsValues() {
        Settings.getInstance().setComponentName(this.componentNameTextField.getText());
        Settings.getInstance().setDomainName(this.domainTextField.getText());
        Settings.getInstance().setSwaggerUrl(this.swaggerUrlTextField.getText());
        Settings.getInstance().setServiceEndpoint(this.serviceEndPointTextField.getText());
    }

    private void buildKotlinApi() {
        RetroswaggerApiConfiguration configuration = new RetroswaggerApiConfiguration(
                this.serviceEndPointTextField.getText(),
                this.swaggerUrlTextField.getText(),
                Settings.getInstance().getPackageName(),
                toFirstUpperCase(this.componentNameTextField.getText()),
                Settings.getInstance().getModuleName(),
                "",
                false
        );

        RetroswaggerApiBuilder kotlinApiBuilder = new RetroswaggerApiBuilder(configuration, errorTracking);
        kotlinApiBuilder.build();
        generateFiles(configuration, kotlinApiBuilder);
    }

    private void generateFiles(RetroswaggerApiConfiguration configuration, RetroswaggerApiBuilder retroswaggerApiBuilder) {
        StorageUtils.generateFiles(
            configuration.getModuleName(),
            configuration.getPackageName(),
            retroswaggerApiBuilder.getGeneratedApiInterfaceTypeSpec()
        );

        for (TypeSpec typeSpec : retroswaggerApiBuilder.getGeneratedModelListTypeSpec()) {
            StorageUtils.generateFiles(configuration.getModuleName(), configuration.getPackageName(), typeSpec);
        }

        for (TypeSpec typeSpec : retroswaggerApiBuilder.getGeneratedEnumListTypeSpec()) {
            StorageUtils.generateFiles(configuration.getModuleName(), configuration.getPackageName(), typeSpec);
        }
    }
}
