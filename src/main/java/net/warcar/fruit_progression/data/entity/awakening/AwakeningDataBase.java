package net.warcar.fruit_progression.data.entity.awakening;

public class AwakeningDataBase implements IAwakeningData {
    private boolean awake;

    public AwakeningDataBase() {
    }

    public boolean isAwakened() {
        return this.awake;
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
    }
}
