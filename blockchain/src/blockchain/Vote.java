package blockchain;

import java.security.PublicKey;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    private Elections elections;
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
    private String id;

    public Vote(PublicKey voter, Elections elections, int answerInt) {
        super();
        this.voter = voter;
        this.elections = elections;
        this.answerInt = answerInt;
        this.answer = null;
    }

    public Vote(PublicKey voter, Elections elections, String answer) {
        super();
        this.voter = voter;
        this.elections = elections;
        this.answer = answer;
        answerInt = -1;
    }

    public Vote(PublicKey voter, Elections elections) {
        this(voter, elections, null);
    }

    public Vote(PublicKey voter) {
        this(voter, null, null);
    }

    @Override
    final public void processEntry(List<Object> inputEntries) throws IllegalArgumentException {
        if(inputEntries.size() != 1) {
            throw new IllegalArgumentException("inputEntries must be of length 1 in new Vote(...)");
        }

        if(inputEntries.get(0) instanceof Elections) {
            this.elections = (Elections) inputEntries.get(0);
        } else {
            throw new IllegalArgumentException("Vote needs Elections to processEntry");
        }
        if(answerInt >= elections.getAnswers().length || answerInt < -1) {
            throw new IllegalArgumentException("answerInt was out of range of possible answers");
        }
        if(!Objects.equals(answer, null)) {
            boolean answerIsPossible = false;
            for(int i = 0; i < elections.getAnswers().length; i++) {
                if(elections.getAnswers()[i].equals(answer)) {
                    answerIsPossible = true;
                    this.answerInt = i;
                    break;
                }
            }
            if(!answerIsPossible) {
                throw new IllegalArgumentException("answer is not an element of possible Elections' answers");
            }
        } else {
            this.answer = elections.getAnswers()[answerInt];
        }

        id = StringUtils.stringToHex(
                StringUtils.keyToString(voter) + elections.getId() + answer + getTimeStamp()
        );
    }

    @Override
    public boolean validateEntry() {
        return getId().equals(StringUtils.stringToHex(
                StringUtils.keyToString(voter) + elections.getId() + answer + getTimeStamp()));
    }

    final public void processEntry() {
        processEntry(Collections.singletonList(elections));
    }

    final public void processEntry(Elections elections) {
        processEntry(Collections.singletonList(elections));
    }

    @Override
    final public String getId() {
        return id;
    }

    final public String getElectionsId() {
        return elections.getId();
    }

    final public String getAnswer() {
        return answer;
    }

    @Override
    final public String toString() {
        return "VoteId: " + getId() + "\nElections: " + getElectionsId() + "\nAnswer: " + getAnswer();
    }
}
