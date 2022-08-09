package com.dotcms.csspreproc.dartsass;

import com.dotmarketing.util.Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows you to enable/disable different configuration parameters that define the behavior of the Dart SASS
 * Compiler. If no parameters are changed upon instantiation, the default dotCMS-specific configuration will be used
 * instead. Here's the list of parameters that can be activated for the compiler, which can be found in the {@code
 * dotmarketing-config.properties} file:
 * <ul>
 *     <li>{@code Verbose}: ({@code false} by default) Prints all deprecation warnings even when they're repetitive
 *     .</li>
 *     <li>{@code Expanded CSS}: ({@code true} by default) Controls the output style of the resulting CSS. Dart Sass
 *     supports two output styles: {@code expanded} (the default) writes each selector and declaration on its own
 *     line. And {@code compressed} removes as many extra characters as possible, and writes the entire stylesheet on
 *     a single line.</li>
 *     <li>{@code Error in CSS}: ({@code true} by default) This flag tells Sass whether to emit a CSS file when an
 *     error occurs during compilation. This CSS file describes the error in a comment and in the "content" property
 *     of "body::before", so that you can see the error message in the browser without needing to switch back to the
 *     terminal.</li>
 *     <li>{@code Stop on Error}: ({@code true} by default) This flag tells Sass to stop compiling immediately when
 *     an error is detected, rather than trying to compile other Sass files that may not contain errors.</li>
 *     <li>{@code Deprecation Warnings}: ({@code false} by default) This flag tells Sass to emit any warnings when
 *     compiling. By default, Sass emits warnings when deprecated features are used or when the {@code @warn} rule is
 *     encountered.</li>
 *     <li>{@code Deprecation Warnings From Dependencies}: ({@code false} by default) This flag tells Sass not to
 *     emit deprecation warnings that come from dependencies. It considers any file that’s transitively imported
 *     through a load path to be a "dependency".</li>
 * </ul>
 *
 * @author Jose Castro
 * @since Jul 25th, 2022
 */
public class CompilerOptions implements Serializable {

    private final boolean verbose;
    private final boolean expandedCss;
    private final boolean errorInCss;
    private final boolean stopOnError;
    private final boolean deprecationWarnings;
    private final boolean deprecationWarningsFromDependencies;

    private static final String VERBOSE = "dartsass.compiler.verbose";
    private static final String ENABLE_EXPANDED_CSS = "dartsass.compiler.expanded.css";
    private static final String ERROR_IN_CSS = "dartsass.compiler.error.in.css";
    private static final String STOP_ON_ERROR = "dartsass.compiler.stop.on.error";
    private static final String DEPRECATION_WARNINGS = "dartsass.compiler.deprecation.warnings";
    private static final String DEPRECATION_WARNINGS_FROM_DEPENDENCIES = "dartsass.compiler.deprecation.warnings.from.dependencies";
    /**
     * Contains the list of commands that are either not required, or only useful when calling the compiler directly
     * from a Terminal.
     */
    private static final List<String> DEFAULT_COMMANDS = List.of("--no-color", "--no-source-map");

    /**
     * Private builder-based class constructor.
     *
     * @param builder The {@link Builder} object containing the specified compilation parameters.
     */
    private CompilerOptions(final Builder builder) {
        this.verbose = builder.verbose;
        this.expandedCss = builder.expandedCss;
        this.errorInCss = builder.errorInCss;
        this.stopOnError = builder.stopOnError;
        this.deprecationWarnings = builder.deprecationWarnings;
        this.deprecationWarningsFromDependencies = builder.deprecationWarningsFromDependencies;
    }

    /**
     * Returns the value of the "verbose" flag.
     *
     * @return Returns {@code true} if enabled.
     */
    public boolean verbose() {
        return this.verbose;
    }

    /**
     * Returns the value of the "Expanded CSS" flag.
     *
     * @return Returns {@code true} if enabled.
     */
    public boolean expandedCss() {
        return this.expandedCss;
    }

    /**
     * Returns the value of the "Error in CSS" flag.
     *
     * @return Returns {@code true} if enabled.
     */
    public boolean errorInCss() {
        return this.errorInCss;
    }

    /**
     * Returns the value of the "Stop on Error" flag.
     *
     * @return Returns {@code true} if enabled.
     */
    public boolean stopOnError() {
        return this.stopOnError;
    }

    /**
     * Returns the value of the "Deprecation Warnings" flag.
     *
     * @return Returns {@code true} if enabled.
     */
    public boolean deprecationWarnings() {
        return this.deprecationWarnings;
    }

    /**
     * Returns the value of the "Deprecation Warnings From Dependencies" flag.
     *
     * @return Returns {@code true} if enabled.
     */
    public boolean deprecationWarningsFromDependencies() {
        return this.deprecationWarningsFromDependencies;
    }

    /**
     * Generates a list with the Dart SASS configuration parameters that were specified when an instance of this class
     * was created.
     *
     * @return A String list containing the expected configuration parameters for the compiler.
     */
    public List<String> generate() {
        final List<String> commands = new ArrayList<>();
        commands.addAll(DEFAULT_COMMANDS);
        if (this.verbose()) {
            commands.add(SassCommands.VERBOSE.enable());
        }
        commands.add(this.expandedCss() ? SassCommands.EXPANDED_CSS.enable() : SassCommands.EXPANDED_CSS.disable());
        commands.add(this.errorInCss() ? SassCommands.ERROR_IN_CSS.enable() : SassCommands.ERROR_IN_CSS.disable());
        if (this.stopOnError()) {
            commands.add(SassCommands.STOP_ON_ERROR.enable());
        }
        if (!this.deprecationWarnings()) {
            commands.add(SassCommands.DEPRECATION_WARNINGS.disable());
        }
        if (!this.deprecationWarningsFromDependencies()) {
            commands.add(SassCommands.DEPRECATION_WARNINGS_FROM_DEPENDENCIES.disable());
        }
        return commands;
    }

    @Override
    public String toString() {
        return "CompilerOptions{" + "verbose=" + verbose + ", expandedCss=" + expandedCss + ", errorInCss="
                       + errorInCss + ", stopOnError=" + stopOnError + ", deprecationWarnings=" + deprecationWarnings
                       + ", deprecationWarningsFromDependencies=" + deprecationWarningsFromDependencies
                       + ", defaultCommands=" + DEFAULT_COMMANDS + '}';
    }

    /**
     * Contains the set of allowed Dart SASS Compiler commands that developers can set up in order to change the
     * behavior of the compiler. It's worth noting that some commands require a specific command to disable them, while
     * other don't.
     */
    private enum SassCommands {

        VERBOSE("--verbose", ""),
        EXPANDED_CSS("--style=expanded", "--style=compressed"),
        ERROR_IN_CSS("--error-css", "--no-error-css"),
        STOP_ON_ERROR("--stop-on-error", ""),
        DEPRECATION_WARNINGS("", "--quiet"),
        DEPRECATION_WARNINGS_FROM_DEPENDENCIES("", "--quiet-deps");

        private final String enabledCommand;
        private final String disabledCommand;

        /**
         * Specifies the appropriate command used to enable and disable this specific property for the Dart SASS
         * Compiler.
         *
         * @param enabledCommand  The command to enable this configuration parameter -- if required.
         * @param disabledCommand The command to enable this configuration parameter -- if required.
         */
        SassCommands(final String enabledCommand, final String disabledCommand) {
            this.enabledCommand = enabledCommand;
            this.disabledCommand = disabledCommand;
        }

        /**
         * Returns the appropriate command to enable this configuration parameter.
         *
         * @return The CLI command.
         */
        public String enable() {
            return this.enabledCommand;
        }

        /**
         * Returns the appropriate command to disable this configuration parameter.
         *
         * @return The CLI command.
         */
        public String disable() {
            return this.disabledCommand;
        }

    }

    /**
     * Allows you to specify the available configuration parameters for executing the Dart SASS Compiler inside dotCMS.
     */
    public static final class Builder {

        private boolean verbose = Config.getBooleanProperty(VERBOSE, Boolean.FALSE);
        private boolean expandedCss = Config.getBooleanProperty(ENABLE_EXPANDED_CSS, Boolean.TRUE);
        private boolean errorInCss = Config.getBooleanProperty(ERROR_IN_CSS, Boolean.TRUE);
        private boolean stopOnError = Config.getBooleanProperty(STOP_ON_ERROR, Boolean.TRUE);
        private boolean deprecationWarnings = Config.getBooleanProperty(DEPRECATION_WARNINGS, Boolean.FALSE);
        private boolean deprecationWarningsFromDependencies =
                Config.getBooleanProperty(DEPRECATION_WARNINGS_FROM_DEPENDENCIES, Boolean.FALSE);

        /**
         * Default class constructor.
         */
        public Builder() {
        }

        /**
         * This flag prints all deprecation warnings even when they're repetitive.
         *
         * @param verbose Set to {@code true} to enable this parameter. Otherwise, set to false.
         *
         * @return The current {@link Builder} instance.
         */
        public Builder verbose(final boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * This flag controls the output style of the resulting CSS. Dart Sass supports two output styles: {@code
         * expanded} (the default) writes each selector and declaration on its own line. And {@code compressed} removes
         * as many extra characters as possible, and writes the entire stylesheet on a single line.
         *
         * @param expandedCss Set to {@code true} to enable this parameter. Otherwise, set to false.
         *
         * @return The current {@link Builder} instance.
         */
        public Builder expandedCss(final boolean expandedCss) {
            this.expandedCss = expandedCss;
            return this;
        }

        /**
         * This flag tells Sass whether to emit a CSS file when an error occurs during compilation. This CSS file
         * describes the error in a comment and in the "content" property of "body::before", so that you can see the
         * error message in the browser without needing to switch back to the terminal.
         *
         * @param errorInCss Set to {@code true} to enable this parameter. Otherwise, set to false.
         *
         * @return The current {@link Builder} instance.
         */
        public Builder errorInCss(final boolean errorInCss) {
            this.errorInCss = errorInCss;
            return this;
        }

        /**
         * This flag tells Sass to stop compiling immediately when an error is detected, rather than trying to
         * compile other Sass files that may not contain errors.
         *
         * @param stopOnError Set to {@code true} to enable this parameter. Otherwise, set to false.*
         *
         * @return The current {@link Builder} instance.
         */
        public Builder stopOnError(final boolean stopOnError) {
            this.stopOnError = stopOnError;
            return this;
        }

        /**
         * This flag tells Sass to emit any warnings when compiling. By default, Sass emits warnings when deprecated
         * features are used or when the {@code @warn} rule is encountered.
         *
         * @param deprecationWarnings Set to {@code true} to enable this parameter. Otherwise, set to false.
         *
         * @return The current {@link Builder} instance.
         */
        public Builder deprecationWarnings(final boolean deprecationWarnings) {
            this.deprecationWarnings = deprecationWarnings;
            return this;
        }

        /**
         * This flag tells Sass not to emit deprecation warnings that come from dependencies. It considers any file
         * that’s transitively imported through a load path to be a "dependency".
         *
         * @param deprecationWarningsFromDependencies Set to {@code true} to enable this parameter. Otherwise, set to
         *                                            false.
         *
         * @return The current {@link Builder} instance.
         */
        public Builder deprecationWarningsFromDependencies(final boolean deprecationWarningsFromDependencies) {
            this.deprecationWarningsFromDependencies = deprecationWarningsFromDependencies;
            return this;
        }

        /**
         * Creates an instance of the {@link CompilerOptions} class witht the specified or default configuration
         * parameters for the Dart SASS Compiler.
         *
         * @return The {@link CompilerOptions} object.
         */
        public CompilerOptions build() {
            return new CompilerOptions(this);
        }

    }

}
