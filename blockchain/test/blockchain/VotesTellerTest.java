package blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class VotesTellerTest {
  VotesTeller teller;
  Elections elections = new Elections(null, "test questions", new String[] {"answer1", "answer2"});
  Vote vote1 = new Vote(null, elections, "answer2");

  @Test
  void constructorDoesNotThrow() {
    Assertions.assertDoesNotThrow(()-> new VotesTeller(elections, new ArrayList<>(List.of(vote1))));
  }
}
