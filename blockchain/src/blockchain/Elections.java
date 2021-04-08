package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

/** Represents a call for Elections.
 *
 */
public class Elections extends Entry {
    /**
     * The public ECDSA key of the account which calls this Elections.
     * The author of the elections.
     */
    private final PublicKey electionCaller;
    /**
     * The 64-digit hex string identifying this Elections.
     */
    private String electionsId;
    /**
     * The question asked in this Elections.
     */
    private String electionsQuestion;
    /**
     * The array of possible answers for this Elections' question.
     * Users answers must be on of those.
     */
    private String[] answers;

    // Constructors
    public Elections(PublicKey electionCaller, String electionsQuestion, String[] answers) {
        super();

        if(electionsQuestion == null) {
            throw new IllegalArgumentException("electionsQuestion cannot be null in new Elections(...)");
        }
        if(answers == null) {
            throw new IllegalArgumentException("answers cannot be null in new Elections(...)");
        }

        this.electionCaller = electionCaller;
        this.electionsQuestion = electionsQuestion;
        this.answers = answers;
    }

    public Elections(PublicKey electionCaller, String electionsQuestion) {
        this(electionCaller, electionsQuestion, new String[] {});
    }

    public Elections(PublicKey electionCaller) {
        this(electionCaller, "", new String[] {});
    }

    // Methods
    /** Processes the input Entries and sets the state of this Elections
     *
     * Elections must have null input entries.
     *
     * @param inputEntries the entries that are passed to this Elections
     * @throws IllegalArgumentException if the inputEntries are different than null
     */
    @Override
    final public void processEntry(ArrayList<Entry> inputEntries) throws IllegalArgumentException {
        if(inputEntries != null) {
            throw new IllegalArgumentException(
                    "inputEntries needs to be null in new Elections(ArrayList<Entry> inputEntries");
        }

        if(answers.length == 0) {
            throw new RuntimeException("The array of answers must be of length > 0");
        }

        electionsId = StringUtils.stringToHex(StringUtils.keyToString(electionCaller) + getTimeStamp() +
                electionsQuestion + Arrays.toString(answers));
    }

    final public void processEntry() throws IllegalArgumentException {
        processEntry(null);
    }

    public final PublicKey getElectionCaller() { return electionCaller; }

    public final String getElectionsId() { return electionsId; }

    public final String getQuestion() {
        return electionsQuestion;
    }
    public final void setQuestion(String electionsQuestion) { this.electionsQuestion = electionsQuestion; }

    public final String[] getAnswers() {
        return answers;
    }
    public final void setAnswers(String[] answers) { this.answers = answers; }

    @Override
    public String toString() {
        return "Elections: " + electionsId +
                "\nQuestion:" + electionsQuestion +
                "\nAnswers: " + Arrays.toString(answers);
    }
}
