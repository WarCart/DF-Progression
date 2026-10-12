package net.warcar.fruit_progression.equations;

import net.minecraft.world.entity.LivingEntity;

public abstract class Operator {
    public abstract double calculate(LivingEntity entity, Equation[] inputs);
}
