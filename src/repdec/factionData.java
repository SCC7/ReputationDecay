package repdec;

import com.fs.starfarer.api.Global;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class factionData {
    String faction;
    float equilibrium;
    float decayRate;

    public factionData(String faction, float equilibrium, float decayRate) {
        this.faction = faction;
        this.equilibrium = equilibrium;
        this.decayRate = decayRate;
    }


    void writeFaction (String faction) {
        this.faction = faction;
    }
    void writeEquilibrium (float equilibrium) {
        this.equilibrium = equilibrium;
    }
    void writeDecayRate (float decayRate) {
        this.decayRate = decayRate;
    }
    String getFaction() {
        return faction;
    }
    float getEquilibrium() {
        return equilibrium;
    }
    float getDecayRate() {
        return decayRate;
    }
}
