package repdec;

import com.fs.starfarer.api.BaseModPlugin;

public class ReputationDecayPlugin extends BaseModPlugin {
    @Override
    public void onNewGameAfterTimePass() {
        onGameLoad(true);
    }

    @Override
    public void onGameLoad(boolean newGame) {
        new ReputationDecayMonthly();
    }

}
