package fuzs.sneakycurses.capability;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;

public class CursedTridentCapability implements CapabilityComponent {
    private boolean isCursed;

    public boolean isCursed() {
        return this.isCursed;
    }

    public void setCursed(boolean cursed) {
        this.isCursed = cursed;
    }
}
