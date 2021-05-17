package org.votebloke.blockchain;

import static org.mockito.Mockito.when;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TallyTest {
  @Mock VotesTeller votesTellerMock;

  PublicKey teller;

  {
    try {
      teller = Account.generateKeys().getPublic();
    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
  }

  Elections elections =
      new Elections(teller, "test questions", new String[] {"answer1", "answer2"});
  Vote vote1 = new Vote(teller, elections, "answer2");
  ArrayList<Vote> votesArray = new ArrayList<>();

  Tally tally;

  @BeforeEach
  void setUp() {
    elections.processEntry();
    vote1.processEntry();
    votesArray.add(vote1);
  }

  @Test
  void testTallyConstructorsDoNotThrow() {
    Assertions.assertDoesNotThrow(() -> new Tally(teller));
    Assertions.assertDoesNotThrow(() -> new Tally(teller, elections, votesArray));

    ArrayList<Entry> inputEntries = new ArrayList<>(List.of(elections, vote1));
    Assertions.assertDoesNotThrow(() -> new Tally(teller, inputEntries));
  }

  @Test
  void processEntryDoesNotThrowOnValidInputs() {
    when(votesTellerMock.tallyVotes()).thenReturn(new HashMap<String, String[]>());
    tally = new Tally(teller, elections, votesArray);
    tally.setVotesTeller(votesTellerMock);
    Assertions.assertDoesNotThrow(() -> tally.processEntry());
  }

  @Test
  void processedEntryValidates() {
    when(votesTellerMock.tallyVotes()).thenReturn(new HashMap<String, String[]>());
    tally = new Tally(teller, elections, votesArray);
    tally.setVotesTeller(votesTellerMock);
    tally.processEntry();
    Assertions.assertTrue(tally.validateEntry());
  }

  @Test
  void getEntryTypeReturnsExpectedString() {
    tally = new Tally(teller);
    Assertions.assertEquals("tally", tally.getEntryType());
  }

  @Test
  void getAuthorReturnsExpectedString() {
    tally = new Tally(teller);
    Assertions.assertEquals(StringUtils.keyToString(teller), tally.getAuthor());
  }

  @Test
  void getMetadataReturnsExpectedMap() {
    when(votesTellerMock.tallyVotes())
        .thenReturn(
            new HashMap<String, String[]>(Map.of("test string", new String[] {"test value"})));
    tally = new Tally(teller, elections, votesArray);
    tally.setVotesTeller(votesTellerMock);

    Assertions.assertNull(tally.getMetadata());
    tally.processEntry();
    HashMap<String, String[]> expected =
        new HashMap<String, String[]>(Map.of("test string", new String[] {"test value"}));
    Assertions.assertEquals(expected.keySet(), tally.getMetadata().keySet());
    Assertions.assertArrayEquals(
        expected.get("test string"), tally.getMetadata().get("test string"));
  }
}
