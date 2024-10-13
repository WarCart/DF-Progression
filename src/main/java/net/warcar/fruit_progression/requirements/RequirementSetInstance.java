package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

import java.util.List;

public class RequirementSetInstance {
    public final List<List<RequirementInstance>> reqs;
    public final String name;

    public RequirementSetInstance(List<List<RequirementInstance>> reqs, String name) {
        this.reqs = reqs;
        this.name = name;
    }

    public boolean isFulfilled(LivingEntity player, AbilityCore<?> core) {
        List<List<RequirementInstance>> reqsSquared = this.reqs;
        boolean stoppedOuter = false;
        if (reqsSquared != null) {
            for (List<RequirementInstance> requirements : reqsSquared) {
                boolean stoppedInner = true;
                for (RequirementInstance requirement : requirements) {
                    boolean reqOutput = requirement.getCore().requirementMet(player, core, requirement);
                    if (requirement.isInverted()) {
                        reqOutput = !reqOutput;
                    }
                    stoppedInner = stoppedInner && reqOutput;
                }
                stoppedOuter = stoppedOuter || stoppedInner;
            }
        }
        return stoppedOuter;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);
        builder.append(", [");
        for (List<RequirementInstance> requirements : this.reqs) {
            builder.append("[");
            for (RequirementInstance requirement : requirements) {
                builder.append(requirement);
                builder.append(", ");
            }
            builder.append("], ");
        }
        builder.append("]");
        return builder.toString();
    }
}
