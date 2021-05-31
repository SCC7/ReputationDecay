package repdec;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.campaign.RepLevel;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.*;


public class ReputationDecayMonthly implements EconomyTickListener {
    //double decayInPercents = 1;
    //float decay;
    //float defaultEquilibrium = 0;
    //float pirateEquilibrium = -0.5f;
    //final int csvFactionID = 0;
    //final float defaultEquilibrium = 0;
    final float defaultDecay = 0;
    //List<String> factionList = null;

    //JSONArray factionSettings;

    boolean repThresholdCooperative = true;
    boolean repThresholdVengeful = true;


    Map<String, Float> reputationLastMonth = new HashMap<String, Float>();
    ArrayList<factionData> factionSettings;
    //Map<String, List<Float>> factionSettings = new HashMap<>();
    //ArrayList<String> pirateFactions = new ArrayList<String>(Arrays.asList(Factions.PIRATES, Factions.LUDDIC_PATH));

    //boolean pirateAndPatherDecayToHostile = true;
    //float safetyMargin = 0.01f;
    //float reputationLastMonth;
    //Map<String, Boolean> hasRepChangedSinceLastMonth = new HashMap<String, Boolean>();
    //boolean hasRepChangedSinceLastMonth;

    public static Logger log = Global.getLogger(ReputationDecayMonthly.class);

    public ReputationDecayMonthly() throws IOException, JSONException {
        Global.getSector().getListenerManager().addListener(this, true);


            //int i = 0;
            JSONObject settings = Global.getSettings().getMergedJSONForMod("repdecSettings.json", "repdec");
            repThresholdVengeful = settings.optBoolean("repThresholdVengeful", repThresholdVengeful);
            repThresholdCooperative = settings.optBoolean("repThresholdVengeful", repThresholdCooperative);

            factionSettings = dataFromCsv.getDataFromCsv(Global.getSettings().getMergedSpreadsheetDataForMod("faction", "data/config/repdec/factionRepdecSettings.csv", "repdec"));
        for (factionData factionSetting : factionSettings) {
            log.info("PRE DEFAULTS Faction: " + factionSetting.getFaction() + ", decay rate: " + factionSetting.getDecayRate() + ", equilibrium point: " + factionSetting.getEquilibrium());
        }
        factionData tempData;
        try {
            //pirateAndPatherDecayToHostile = settings.optBoolean("pirateAndPatherDecayToHostile", pirateAndPatherDecayToHostile);
            //decayInPercents = settings.optDouble("monthlyDecayRate", decayInPercents);
            //decay = (float) decayInPercents/100;
            //if(!settings.optBoolean("pirateAndPatherDecayToHostile")) pirateFactions.clear();
            //new dataFromCsv();
            boolean factionFound = false;
            for (FactionAPI faction : Global.getSector().getAllFactions()) {
                reputationLastMonth.put(faction.getId(), faction.getRelationship(Factions.PLAYER));
                for (factionData factionSetting : factionSettings) {
                    if (factionSetting.getFaction().equals(faction.getId())) factionFound = true;
                }
                if (!factionFound) {
                    /*tempData.writeFaction(faction.getId());
                    tempData.writeDecayRate(defaultDecay);
                    tempData.writeEquilibrium(defaultDecay);*/
                    tempData = new factionData(faction.getId(), defaultDecay, defaultDecay);
                    factionSettings.add(tempData);
                }
                factionFound = false;
                //factionList.add(i, faction.getId());
                //hasRepChangedSinceLastMonth.put(faction.getId(), Boolean.TRUE);
            }
            //factionSettings = Global.getSettings().getMergedSpreadsheetDataForMod("faction", "data/config/repdec/factionRepdecSettings.csv", "repdec");
            //extractCsvData(Global.getSettings().getMergedSpreadsheetDataForMod("faction", "data/config/repdec/factionRepdecSettings.csv", "repdec"));
        }
        catch(Exception e)
        {
            throw new RuntimeException("Reputation Decay: failed to load config: " + e.getMessage(), e);
        }
        for (factionData factionSetting : factionSettings) {
            log.info("DEFAULTS Faction: " + factionSetting.getFaction() + ", decay rate: " + factionSetting.getDecayRate() + ", equilibrium point: " + factionSetting.getEquilibrium());
        }
    }

    @Override
    public void reportEconomyTick (int iterIndex) {}

    @Override
    public void reportEconomyMonthEnd() {
        for (FactionAPI faction : Global.getSector().getAllFactions()) {
            //if(reputationLastMonth.get(faction.getId()) == faction.getRelationship(Factions.PLAYER)) {
                decayRep(faction);
            //}
            int index = 0;
            for (int i=0; i<factionSettings.size(); i++) {
                if(factionSettings.get(i).getFaction().equals(faction.getId())) {
                    index=i;
                    break;
                }
            }
            if(faction.getRelationship(Factions.PLAYER)>0) {
                if (faction.getRelationship(Factions.PLAYER) - Math.signum(faction.getRelationship(Factions.PLAYER)) * factionSettings.get(index).getDecayRate() != Math.signum(faction.getRelationship(Factions.PLAYER))) faction.setRelationship(Factions.PLAYER, 0);
                reputationLastMonth.put(faction.getId(), faction.getRelationship(Factions.PLAYER) - Math.signum(faction.getRelationship(Factions.PLAYER)) * factionSettings.get(index).getDecayRate());
            }
            //hasRepChangedSinceLastMonth = false;
            //log.info(factionSettings);
        }
    }
    void decayRep(FactionAPI faction) {
        //if(faction.getId().equals(Factions.PLAYER)) return;
        //if(decay == 0) return;
        int index=0;
        for (int i=0; i<factionSettings.size(); i++) {
            if(factionSettings.get(i).getFaction().equals(faction.getId())) {
                if(factionSettings.get(i).getDecayRate()==0) return;
                index=i;
                break;
            }
        }
        float decay = factionSettings.get(index).getDecayRate() / 100;
        float equilibrium = factionSettings.get(index).getEquilibrium() / 100;

        if (repThresholdCooperative) {
            if(faction.getRelationship(Factions.PLAYER) >= RepLevel.COOPERATIVE.getMin() + 0.01f
                    && faction.getRelationship(Factions.PLAYER) <= (RepLevel.COOPERATIVE.getMin() + 0.01f + decay)) {
                faction.setRelationship(Factions.PLAYER, RepLevel.COOPERATIVE.getMin() + 0.01f);
                return;
            }
        }
        if (repThresholdVengeful) {
            if(faction.getRelationship(Factions.PLAYER) <= RepLevel.VENGEFUL.getMin() + 0.01f
                    && faction.getRelationship(Factions.PLAYER) >= (RepLevel.VENGEFUL.getMin() + 0.01f + decay)) {
                faction.setRelationship(Factions.PLAYER, RepLevel.VENGEFUL.getMin() + 0.01f);
                return;
            }
        }
        if (faction.getRelationship(Factions.PLAYER) > (equilibrium + decay)) {
            faction.adjustRelationship(Factions.PLAYER, -decay);
            return;
        }
        if (faction.getRelationship(Factions.PLAYER) < (equilibrium - decay)) {
            faction.adjustRelationship(Factions.PLAYER, decay);
            return;
        }
        faction.setRelationship(Factions.PLAYER, equilibrium);
    }
    /*void getRel(FactionAPI faction) {
        faction.getRelationship(Factions.PLAYER);
    }
    void setRel (FactionAPI faction, float value) {
        faction.setRelationship(Factions.PLAYER, value);
    }*/
}
