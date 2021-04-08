package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;

/** Represents a single vote in particular elections.
 *
 */
public class Vote extends Entry {
    /**
     * The public ECDSA key of the account which votes.
     */
    private final PublicKey voter;
    /**
     * The id of Elections this Vote was cast in.
     */
    private final String electionsId;
    /**
     * The voter's answer.
     */
    private String answer;
    /**
     * The voter's answer (as an index of the array of possible answer for the Elections).
     */
    private int answerInt;
    /**
     * The id of this Vote.
     */
    private String voteId;

    public Vote(PublicKey voter, String electionsId, int answerInt) {
        super();
        this.voter = voter;
        this.electionsId = electionsId;
        this.answerInt = answerInt;
        this.answer = "";
    }

    public Vote(PublicKey voter, String electionsId, String answer) {
        super();
        this.voter = voter;
        this.electionsId = electionsId;
        this.answer = answer;
        answerInt = -1;
    }

    @Override
    final public void processEntry(ArrayList<Entry> inputEntries) throws IllegalArgumentException {
        if(inputEntries.size() != 1) {
            throw new IllegalArgumentException("inputEntries must be of length 1 in new Vote(...)");
        }

        Elections elections;
        if(inputEntries.get(0) instanceof Elections) {
            elections = (Elections) inputEntries.get(0);
        } else {
            throw new IllegalArgumentException("Vote needs Elections to processEntry");
        }
        if(electionsId != elections.getId()) {
            throw new IllegalArgumentException("Vote.electionsId does not match Elections' id");
        }
        if(answerInt >= elections.getAnswers().length || answerInt < -1) {
            throw new IllegalArgumentException("answerInt was out of range of possible answers");
        }
        if(answer != "") {
            boolean answerIsPossible = false;
            for(int i = 0; i < elections.getAnswers().length; i++) {
                if(elections.getAnswers()[i].equals(answer)) {
                    answerIsPossible = true;
                    this.answerInt = i;
                    break;
                }
            }
            if(answerIsPossible == false) {
                throw new IllegalArgumentException("answer is not an element of possible Elections' answers");
            }
        } else {
            this.answer = elections.getAnswers()[answerInt];
        }

        voteId = StringUtils.stringToHex(
                StringUtils.keyToString(voter) + electionsId + answer + getTimeStamp()
        );
    }

    @Override
    final public String getId() {
        return voteId;
    }
}
