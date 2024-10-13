package net.warcar.fruit_progression.requirements;

import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Arrays;

public class RequirementInstance {
    private final Requirement core;
    private String[] args;
    private boolean inverted;
    private boolean debug;

    public RequirementInstance(Requirement core) {
        this.core = core;
    }

    public String[] getValues() {
        return args;
    }

    public Requirement getCore() {
        return core;
    }

    public void setValues(String... args) {
        this.args = args;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.debug) {
            builder.append("D");
        }
        if (this.inverted) {
            builder.append("!");
        }
        builder.append(GameRegistry.findRegistry(Requirement.class).getKey(core));
        builder.append(", ");
        builder.append(Arrays.toString(args));
        return builder.toString();
    }
}
