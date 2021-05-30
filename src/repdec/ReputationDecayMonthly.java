package repdec;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.campaign.RepLevel;
//import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ReputationDecayMonthly implements EconomyTickListener {
    double decayInPercents = 1;
    float decay;
    boolean repThresholdCooperative = true;
    boolean repThresholdVengeful = true;
    //float safetyMargin = 0.01f;
    //float reputationLastMonth;
    Map<String, Float> reputationLastMonth = new HashMap<String, Float>();
    //Map<String, Boolean> hasRepChangedSinceLastMonth = new HashMap<String, Boolean>();
    //boolean hasRepChangedSinceLastMonth;

    //public static Logger log = Global.getLogger(ReputationDecayMonthly.class);

    public ReputationDecayMonthly() throws IOException, JSONException {
        Global.getSector().getListenerManager().addListener(this, true);

        try {
            JSONObject settings = Global.getSettings().getMergedJSONForMod("repdecSettings.json", "repdec");
            repThresholdVengeful = settings.optBoolean("repThresholdVengeful", repThresholdVengeful);
            repThresholdCooperative = settings.optBoolean("repThresholdVengeful", repThresholdCooperative);
            decayInPercents = settings.optDouble("monthlyDecayRate", decayInPercents);
            decay = (float) decayInPercents/100;
            for (FactionAPI faction : Global.getSector().getAllFactions()) {
                reputationLastMonth.put(faction.getId(), faction.getRelationship(Factions.PLAYER));
                //hasRepChangedSinceLastMonth.put(faction.getId(), Boolean.TRUE);
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException("Reputation Decay: failed to load config: " + e.getMessage(), e);
        }
    }

    @Override
    public void reportEconomyTick (int iterIndex) {}

    @Override
    public void reportEconomyMonthEnd() {
        for (FactionAPI faction : Global.getSector().getAllFactions()) {
            if(reputationLastMonth.get(faction.getId()).equals(faction.getRelationship(Factions.PLAYER))) {
                if (repThresholdCooperative) {
                    if(faction.getRelationship(Factions.PLAYER) >= RepLevel.COOPERATIVE.getMin() + 0.01f
                            && faction.getRelationship(Factions.PLAYER) <= (RepLevel.COOPERATIVE.getMin() + decay)) {
                        faction.setRelationship(Factions.PLAYER, RepLevel.COOPERATIVE.getMin() + 0.01f);
                        continue;
                    }
                }
                if (repThresholdVengeful) {
                    if(faction.getRelationship(Factions.PLAYER) <= RepLevel.VENGEFUL.getMin() + 0.01f
                            && faction.getRelationship(Factions.PLAYER) >= (RepLevel.VENGEFUL.getMin() + decay)) {
                        faction.setRelationship(Factions.PLAYER, RepLevel.VENGEFUL.getMin() + 0.01f);
                        continue;
                    }
                }
                if (faction.getRelationship(Factions.PLAYER) > decay) {
                    faction.adjustRelationship(Factions.PLAYER, -decay);
                    continue;
                }
                if (faction.getRelationship(Factions.PLAYER) < -decay) {
                    faction.adjustRelationship(Factions.PLAYER, decay);
                    continue;
                }
                faction.setRelationship(Factions.PLAYER, 0);
            }
            if(faction.getRelationship(Factions.PLAYER)>0) reputationLastMonth.put(faction.getId(), faction.getRelationship(Factions.PLAYER) - decay);
            if(faction.getRelationship(Factions.PLAYER)<0) reputationLastMonth.put(faction.getId(), faction.getRelationship(Factions.PLAYER) + decay);
            //hasRepChangedSinceLastMonth = false;
        }
    }
}
