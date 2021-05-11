package blockchain;

import java.security.PublicKey;
import java.util.*;

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

    this.electionCaller = electionCaller;
    setQuestion(electionsQuestion);
    setAnswers(answers);
  }

  public Elections(PublicKey electionCaller, String electionsQuestion) {
    this(electionCaller, electionsQuestion, new String[] {});
  }

  public Elections(PublicKey electionCaller) {
    this(electionCaller, "", new String[] {});
  }

  // Public
  /**
   * Sets the id for this Elections. Performs basic checks on the question and answers in this
   * Elections.
   *
   * <p>Elections must have null input entries.
   *
   * @param inputEntries the entries that are passed to this Elections
   * @return this Elections object
   * @throws IllegalArgumentException if the inputEntries are different than null
   * @throws RuntimeException if the array of answers is of length < 1
   */
  @Override
  public final ArrayList<TransactionOutput> processEntry(List<TransactionInput> inputEntries)
      throws IllegalArgumentException, RuntimeException {
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

    return new ArrayList<>();
  }

  /**
   * Performs basic checks on the question and answers in this Elections. Sets the id for this
   * Elections.
   *
   * <p>Elections must have null input entries. Elections should be processed before adding them to
   * a Transaction objects.
   *
   * @return this Elections object
   * @throws IllegalArgumentException if the inputEntries are different than null * @throws
   * @throws RuntimeException if the array of answers is of length < 1
   */
  public final ArrayList<TransactionOutput> processEntry()
      throws IllegalArgumentException, RuntimeException {
    return (processEntry(null));
  }

  /**
   * Validates this Elections.
   *
   * @return true if the recalculated id of this Elections matches the current id of this Elections
   */
  @Override
  public boolean validateEntry() {
    try {
      return getId()
          .equals(
              StringUtils.hashString(
                  StringUtils.keyToString(electionCaller)
                      + getTimeStamp()
                      + electionsQuestion
                      + Arrays.toString(answers)));
    } catch (Exception e) {
      return false;
    }
  }

  public final PublicKey getElectionsCaller() {
    return electionCaller;
  }

  @Override
  public final String getId() {
    return id;
  }

  @Override
  public String getEntryType() {
    return "elections";
  }

  @Override
  public String getAuthor() {
    return StringUtils.keyToString(this.electionCaller);
  }

    @Override
    public HashMap<String, String[]> getMetadata() {
        return new HashMap<String, String[]>(Map.<String, String[]>of(
                "question", new String[] {this.electionsQuestion},
                "answers", this.answers
        ));
    }

    public final String getQuestion() {
    return electionsQuestion;
  }

  /**
   * Setter of the question posed in this Elections.
   *
   * @param electionsQuestion the question posed in this Elections
   * @throws IllegalArgumentException if the passed question is null
   */
  public final void setQuestion(String electionsQuestion) throws IllegalArgumentException {
    if (electionsQuestion != null) {
      this.electionsQuestion = electionsQuestion;
    } else {
      throw new IllegalArgumentException("electionsQuestion must not be null");
    }
  }

  public final String[] getAnswers() {
    return answers;
  }

  /**
   * Setter of answers for this Elections.
   *
   * @param answers the array of answer for this Elections
   * @throws IllegalArgumentException when the passed array is null
   */
  public final void setAnswers(String[] answers) throws IllegalArgumentException {
    if (answers != null) {
      this.answers = answers;
    } else {
      throw new IllegalArgumentException("answer must not be null");
    }
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
