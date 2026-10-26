package frostdustry.type;

public interface HeatReciever {
    void recieveHeat(float heat, float duration);
    boolean canBeHeated();
}
