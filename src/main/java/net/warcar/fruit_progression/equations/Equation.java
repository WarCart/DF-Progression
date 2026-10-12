package net.warcar.fruit_progression.equations;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.LivingEntity;

public class Equation {
    private boolean changes = true;
    private Double value = null;
    private Operator operator;
    private Equation[] inputs;

    public double calculate(LivingEntity entity) {
        if (!this.changes) {
            if (value == null) {
                value = this.operator.calculate(entity, inputs);
            }
            return value;
        }
        return this.operator.calculate(entity, inputs);
    }

    public static Equation deserialize(JsonElement json) {
        Equation equation = new Equation();
        return equation;
    }
}
