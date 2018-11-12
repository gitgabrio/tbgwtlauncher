package net.cardosi.tbgwtlauncher;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import net.cardosi.tbgwtlauncher.enums.SIDE;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TBGWTRunConfiguration extends RunConfigurationBase {

    private static SIDE side = SIDE.CLIENT;

    protected TBGWTRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new TBGWTSettingsEditor();
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        System.out.println("checkConfiguration");
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        System.out.println("getState " + executor.getId() + " " + executionEnvironment);
        return getCommandLineState(this, executionEnvironment);
    }

    private CommandLineState getCommandLineState(final TBGWTRunConfiguration instance, @NotNull ExecutionEnvironment executionEnvironment) {
        System.out.println("getCommandLineState " + executionEnvironment);
        return new CommandLineState(executionEnvironment) {
            @NotNull
            @Override
            protected ProcessHandler startProcess() throws ExecutionException {
                System.out.println("startProcess");
                final GeneralCommandLine codeServerLine = new GeneralCommandLine();
                codeServerLine.setRedirectErrorStream(true);
                codeServerLine.setExePath("mvn");
                switch (side) {
                    case SERVER:
                        codeServerLine.addParameters("jetty:run", "-pl", "*-server", "-am", "-Denv=dev");
                        break;
                    case CLIENT:
                    default:
                        codeServerLine.addParameters("clean", "gwt:codeserver", "-pl", "*-client", "-am");
                }
                System.out.println("command: " + codeServerLine.getCommandLineString());
                final Process process = codeServerLine.createProcess();
                return new OSProcessHandler(process, codeServerLine.getCommandLineString());
            }

            @NotNull
            @Override
            public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
                final ExecutionResult toReturn = super.execute(executor, runner);
                if (SIDE.CLIENT.equals(side)) {
                    System.out.println("executing on SERVER side");
                    side = SIDE.SERVER;
                    final RunnerAndConfigurationSettingsImpl settings = new RunnerAndConfigurationSettingsImpl(RunManagerImpl.getInstanceImpl(getProject()), instance);
                    ExecutionUtil.runConfiguration(settings, executor);
                }
                return toReturn;
            }
        };
    }
}