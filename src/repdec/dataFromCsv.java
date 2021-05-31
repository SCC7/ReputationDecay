package repdec;

import com.fs.starfarer.api.Global;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class dataFromCsv {
    public static ArrayList<factionData> getDataFromCsv(JSONArray file) throws JSONException {
        //file = Global.getSettings().getMergedSpreadsheetDataForMod("faction", "data/config/repdec/factionRepdecSettings.csv", "repdec");
        //ArrayList<String> givenFaction = new ArrayList<String>();
        //List<Float> tempList = null;
        ArrayList<factionData> factionSettings = new ArrayList<factionData>(); //ArrayList is precious
        factionData tempData;// = new factionData("",0,0);

        for (int i=0; i<file.length(); i++) {
            JSONObject row = file.getJSONObject(i);
            /*tempData.writeFaction(row.getString("faction"));
            tempData.writeEquilibrium((float) row.getDouble("equilibrium"));
            tempData.writeDecayRate((float) row.getDouble("decayRate"));*/
            tempData = new factionData(row.getString("faction"), (float) row.getDouble("equilibrium"), (float) row.getDouble("decayRate"));
            factionSettings.add(tempData);
            //givenFaction.clear();
            //givenFaction.add("faction", row.getString("faction"));
        }
        return factionSettings;
    }
    /*public static Boolean checkIfPresent(ArrayList<factionData> factionSettings, String faction) {
        for (int i = 0; i< Global.getSector().getAllFactions().size(); i++) {
            if(factionSettings.get(i).getFaction().equals(faction)) return true;
        }
        return false;
    }*/
}
