package repdec;

import com.fs.starfarer.api.BaseModPlugin;
import org.json.JSONException;

import java.io.IOException;

public class ReputationDecayPlugin extends BaseModPlugin {
    @Override
    public void onNewGameAfterTimePass() {
        onGameLoad(true);
    }

    @Override
    public void onGameLoad(boolean newGame) {
        try {
            new ReputationDecayMonthly();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}
