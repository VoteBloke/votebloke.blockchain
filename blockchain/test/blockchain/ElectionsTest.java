package blockchain;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ElectionsTest {
  PublicKey author;

  {
    try {
      author = KeyPairGenerator.getInstance("DSA").generateKeyPair().getPublic();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  Elections elections;
  Elections electionsOnlyQuestion;
  Elections electionsFull;

  @BeforeEach
  void setUp() {
    elections = new Elections(author);
    electionsOnlyQuestion = new Elections(author, "test question");
    electionsFull = new Elections(author, "test questions", new String[] {"answer1"});
  }

  @Test
  public void electionsConstructors() {
    assertDoesNotThrow(() -> new Elections(author));
    assertDoesNotThrow(() -> new Elections(author, "Test question"));
    assertDoesNotThrow(() -> new Elections(author, "test question", new String[] {"test answer1"}));
  }

  @Test
  public void electionsIllegalArgumentWhenNullInConstructor() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new Elections(author, null);
        });
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new Elections(author, "question test", null);
        });
  }

  @Test
  public void electionsNullIdIfNotProcessed() {
    assertNull(elections.getId());
  }

  @Test
  public void electionsGetters() {
    assertEquals(author, elections.getElectionsCaller());
    assertEquals("test question", electionsOnlyQuestion.getQuestion());
    assertEquals("test questions", electionsFull.getQuestion());
    assertArrayEquals(new String[] {}, electionsOnlyQuestion.getAnswers());
    assertArrayEquals(new String[] {"answer1"}, electionsFull.getAnswers());
  }

  @Test
  public void electionsSetter() {
    elections.setQuestion("test question");
    assertEquals("test question", elections.getQuestion());
    elections.setAnswers(new String[] {"answer1", "answer2"});
    assertArrayEquals(new String[] {"answer1", "answer2"}, elections.getAnswers());
  }

  @Test
  public void electionsSetQuestionIllegalArgExceptionWhenNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          elections.setQuestion(null);
        });
  }

  @Test
  public void electionsSetAnswersIllegalArgExceptionWhenNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          elections.setAnswers(null);
        });
  }

  @Test
  public void electionsIllegalArgumentWhenNotNullInProcessEntry() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          elections.processEntry(
              new ArrayList<TransactionInput>(
                  List.of(new TransactionInput(new TransactionOutput(author, elections)))));
        });
  }

  @Test
  public void electionsRuntimeExceptionWhenAnswersArrayHasNoElements() {
    assertThrows(
        RuntimeException.class,
        () -> {
          elections.setAnswers(new String[] {});
          elections.processEntry(null);
        });
  }

  @Test
  public void electionsProcessEntryAnswersAndQuestionSet() {
    assertDoesNotThrow(
        () -> {
          electionsFull.processEntry(null);
        });
    assertNotNull(electionsFull.getId());
    assertEquals(64, electionsFull.getId().length());
  }

  @Test
  public void electionsProcessEntryNoArgumentsOverload() {
    assertDoesNotThrow(
        () -> {
          electionsFull.processEntry();
        });
  }

  @Test
  public void electionsValidateFalseBeforeProcessEntry() {
    assertFalse(elections.validateEntry());
  }

  @Test
  public void electionsValidateTrueAfterProcessEntry() {
    electionsFull.processEntry();
    assertTrue(electionsFull.validateEntry());
  }

  @Test
  public void electionsValidateFalseAfterProcessEntryAndModification() {
    electionsFull.processEntry();
    assertTrue(electionsFull.validateEntry());
    electionsFull.setQuestion("a new test question");
    assertFalse(electionsFull.validateEntry());
    electionsFull.processEntry();
    assertTrue(electionsFull.validateEntry());
    electionsFull.setAnswers(new String[] {"answer1", "answer2"});
    assertFalse(electionsFull.validateEntry());
  }

  @Test
  public void electionsProcessEntryReturnsArrayWithItself() {
    ArrayList<TransactionOutput> processedEntry = electionsFull.processEntry();
    assertEquals(1, processedEntry.size());
    assertEquals(electionsFull, processedEntry.get(0).data);
    assertEquals(author, processedEntry.get(0).author);
  }
}
