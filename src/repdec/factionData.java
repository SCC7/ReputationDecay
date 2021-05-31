package repdec;

import com.fs.starfarer.api.Global;

import java.util.*;

public class factionData {
    String faction;
    float equilibrium;
    float decayRate;

    public factionData(String faction, float equilibrium, float decayRate) {
        this.faction = faction;
        this.equilibrium = equilibrium;
        this.decayRate = decayRate;
    }
    /*public Boolean checkIfPresent(ArrayList<factionData> factionSettings, String faction) {
        for (int i = 0; i< Global.getSector().getAllFactions().size(); i++) {
            if(factionSettings.get(i).getFaction().equals(faction)) return true;
        }
        return false;
    }*/


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
