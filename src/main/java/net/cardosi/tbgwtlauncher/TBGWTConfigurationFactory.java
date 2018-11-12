package net.cardosi.tbgwtlauncher;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import net.cardosi.tbgwtlauncher.enums.SIDE;

public class TBGWTConfigurationFactory extends ConfigurationFactory {

    private final String FACTORY_NAME = "TBGWTlauncher configuration factory";
    private SIDE side;

    protected TBGWTConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    public RunConfiguration createTemplateConfiguration(Project project) {
        return new TBGWTRunConfiguration(project, this, "TBGWT");
    }

    @Override
    public String getName() {
        return FACTORY_NAME;
    }
}