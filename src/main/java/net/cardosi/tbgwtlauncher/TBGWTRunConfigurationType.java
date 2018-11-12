package net.cardosi.tbgwtlauncher;

import javax.swing.Icon;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import org.jetbrains.annotations.NotNull;

public class TBGWTRunConfigurationType implements ConfigurationType {
    @Override
    public String getDisplayName() {
        return "TBGWT";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "TBGWT Run Configuration Type";
    }

    @Override
    public Icon getIcon() {
        return TBGWTPluginIcons.TBGWT_ICON;
    }

    @NotNull
    @Override
    public String getId() {
        return "TBGWT_RUN_CONFIGURATION";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new TBGWTConfigurationFactory(this)};
    }
}