package blockchain;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

/** Represents a call for Elections. */
public class Elections extends Entry {
  /**
   * The public ECDSA key of the account which calls this Elections. The author of the elections.
   */
  private final PublicKey electionCaller;
  /** The 64-digit hex string identifying this Elections. */
  private String id;
  /** The question asked in this Elections. */
  private String electionsQuestion;
  /**
   * The array of possible answers for this Elections' question. Users answers must be on of those.
   */
  private String[] answers;

  /**
   * The constructor for Elections.
   *
   * @param electionCaller the public ECDSA key of the Account starting this Elections
   * @param electionsQuestion the question posed in this Elections
   * @param answers the array of possible answers to the question asked in this Elections
   */
  // Constructors
  public Elections(PublicKey electionCaller, String electionsQuestion, String[] answers) {
    super();

    if (electionsQuestion == null) {
      throw new IllegalArgumentException("electionsQuestion cannot be null in new Elections(...)");
    }
    if (answers == null) {
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
  /**
   * Sets the id for this Elections. Performs basic checks on the question and answers in this
   * Elections.
   *
   * <p>Elections must have null input entries.
   *
   * @param inputEntries the entries that are passed to this Elections
   * @throws IllegalArgumentException if the inputEntries are different than null
   */
  @Override
  public final void processEntry(List<Object> inputEntries) throws IllegalArgumentException {
    if (inputEntries != null) {
      throw new IllegalArgumentException(
          "inputEntries needs to be null in new Elections(ArrayList<Entry> inputEntries");
    }

    if (answers.length == 0) {
      throw new RuntimeException("The array of answers must be of length > 0");
    }

    id =
        StringUtils.hashString(
            StringUtils.keyToString(electionCaller)
                + getTimeStamp()
                + electionsQuestion
                + Arrays.toString(answers));
  }

  /**
   * Performs basic checks on the question and answers in this Elections. Sets the id for this
   * Elections.
   *
   * <p>Elections must have null input entries. Elections should be processed before adding them to
   * a Transaction objects.
   *
   * @throws IllegalArgumentException if the inputEntries are different than null
   */
  public final void processEntry() throws IllegalArgumentException {
    processEntry(null);
  }

  /**
   * Validates this Elections.
   *
   * @return true if the recalculated id of this Elections matches the current id of this Elections
   */
  @Override
  public boolean validateEntry() {
    return getId()
        .equals(
            StringUtils.hashString(
                StringUtils.keyToString(electionCaller)
                    + getTimeStamp()
                    + electionsQuestion
                    + Arrays.toString(answers)));
  }

  public final PublicKey getElectionsCaller() {
    return electionCaller;
  }

  @Override
  public final String getId() {
    return id;
  }

  public final String getQuestion() {
    return electionsQuestion;
  }

  public final void setQuestion(String electionsQuestion) {
    this.electionsQuestion = electionsQuestion;
  }

  public final String[] getAnswers() {
    return answers;
  }

  public final void setAnswers(String[] answers) {
    this.answers = answers;
  }

  @Override
  public final String toString() {
    return "Elections: "
        + id
        + "\nQuestion:"
        + electionsQuestion
        + "\nAnswers: "
        + Arrays.toString(answers);
  }
}
