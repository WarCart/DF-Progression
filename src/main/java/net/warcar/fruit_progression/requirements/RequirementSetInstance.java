package net.warcar.fruit_progression.requirements;

import java.util.List;

public class RequirementSetInstance {
    public final List<List<RequirementInstance>> reqs;
    public final String name;

    public RequirementSetInstance(List<List<RequirementInstance>> reqs, String name) {
        this.reqs = reqs;
        this.name = name;
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
