package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Tally extends Entry {
    private String id;
    private Elections elections;
    private List<Vote> votes;
    private PublicKey teller;

    public Tally(PublicKey teller, List<Object> inputEntries) throws IllegalArgumentException {
        super();
        this.teller = teller;
        setElections(inputEntries.get(0));
        setVotes(inputEntries.subList(1, inputEntries.size() - 1));
    }

    public Tally(PublicKey teller, Elections elections, List<Vote> votes) {
        super();
        this.teller = teller;
        this.elections = elections;
        this.votes = votes;
    }

    public Tally(PublicKey teller) {
        this(teller, null, null);
    }

    @Override
    final public void processEntry(List<Object> inputEntries) throws IllegalArgumentException {
        processEntry(inputEntries.get(0), inputEntries.subList(1, inputEntries.size() - 1));
    }

    @Override
    public boolean validateEntry() {
        return getId().equals(StringUtils.stringToHex(
                StringUtils.keyToString(teller) +
                        elections.getId() +
                        votes.toString() +
                        getTimeStamp()));
    }

    final public void processEntry(Object elections, List<Object> votes) throws IllegalArgumentException {
        setElections(elections);
        setVotes(votes);
        processEntry();
    }

    final public void processEntry() throws RuntimeException {
        if(!elections.validateEntry()) {
            throw new RuntimeException("Entry elections: " + elections.getId() +
                    " does not validate - its hash does not match its contents");
        }

        for(Vote vote : votes) {
            if(!vote.getElectionsId().equals(elections.getId())) {
                throw new RuntimeException("Vote: " + vote.getId() + " was cast in elections: " +
                        vote.getElectionsId() + " ,but should be cast in: " + elections.getId());
            }
            if(!vote.validateEntry()) {
                throw new RuntimeException("Entry: " + vote.getId() +
                        " does not validate - its hash does not match its contents");
            }
        }
        updateId();
    }

    final public void setElections(Object entry) {
        Elections elections;
        try {
            elections = (Elections) entry;
        } catch (ClassCastException | NullPointerException exception) {
            throw new IllegalArgumentException("entry must be of type Elections");
        }

        this.elections = elections;
    }

    final public void setElections(Elections elections) {
        this.elections = elections;
    }

    final public void setVotes(List<Object> entries) {
        ArrayList<Vote> new_votes = new ArrayList<>();
        for(Object entry : entries) {
            try {
                votes.add((Vote) entry);
            } catch (ClassCastException | NullPointerException exception) {
                throw new IllegalArgumentException("All elements of entries must be of type Vote");
            }
        }
        this.votes = new_votes;
    }

    @Override
    public String getId() {
        return id;
    }

    private void updateId() {
        this.id = StringUtils.stringToHex(
                StringUtils.keyToString(teller) +
                        elections.getId() +
                        votes.toString() +
                        getTimeStamp());
    }
}
