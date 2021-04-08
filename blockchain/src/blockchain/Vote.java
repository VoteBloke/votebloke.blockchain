package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;

public class Vote extends Entry {
    private final PublicKey voter;

    public Vote(PublicKey voter) {
        super();
        this.voter = voter;
    }

    @Override
    public void processEntry(ArrayList<Entry> inputEntries) throws IllegalArgumentException {
    }


}
