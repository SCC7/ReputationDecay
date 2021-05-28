package repdec;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.campaign.RepLevel;
import org.apache.log4j.Logger;


public class ReputationDecayMonthly implements EconomyTickListener {
    float decay = 0.01f;
    boolean repThresholdCooperative = true;
    boolean repThresholdVengeful = true;
    float safetyMargin = 0.01f;

    public static Logger log = Global.getLogger(ReputationDecayMonthly.class);

    public ReputationDecayMonthly() {
        Global.getSector().getListenerManager().addListener(this, true);
    }

    @Override
    public void reportEconomyTick (int iterIndex) {}

    @Override
    public void reportEconomyMonthEnd() {
        for (FactionAPI faction : Global.getSector().getAllFactions()) {
            if (repThresholdCooperative) {
                if(faction.getRelationship(Factions.PLAYER) > RepLevel.COOPERATIVE.getMin()
                && faction.getRelationship(Factions.PLAYER) < (RepLevel.COOPERATIVE.getMin() + safetyMargin)) {
                    faction.setRelationship(Factions.PLAYER, RepLevel.COOPERATIVE.getMin());
                    //log.info("-+= REPUTATION DECAY DEBUG, RepLevel.COOPERATIVE.getMin() is" + RepLevel.COOPERATIVE.getMin());
                  continue;
                }
            }
            if (repThresholdVengeful) {
                if(faction.getRelationship(Factions.PLAYER) < RepLevel.VENGEFUL.getMin()
                && faction.getRelationship(Factions.PLAYER) > (RepLevel.VENGEFUL.getMin() + safetyMargin)) {
                    faction.setRelationship(Factions.PLAYER, RepLevel.VENGEFUL.getMin());
                    //log.info("-+= REPUTATION DECAY DEBUG, RepLevel.COOPERATIVE.getMin() is" + RepLevel.COOPERATIVE.getMin());
                    continue;
                }
            }
            if (faction.getRelationship(Factions.PLAYER)>0) faction.adjustRelationship(Factions.PLAYER, -decay);
            if (faction.getRelationship(Factions.PLAYER)<0) faction.adjustRelationship(Factions.PLAYER, decay);
        }
    }
}
