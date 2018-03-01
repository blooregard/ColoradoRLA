package us.freeandfair.corla.query;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import us.freeandfair.corla.model.County;
import us.freeandfair.corla.model.PartyBallotType;
import us.freeandfair.corla.model.PoliticalParty;

/**
 * 
 * @description <description>
 * @explanation <explanation>
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class PartyBallotTypeQueriesTest {
  
  /**
   * 
   * @description <description>
   * @explanation <explanation>
   */
  @Ignore
  @Test
  public void testMatching() {
    final County a = CountyQueries.fromString("1");
    final List<PartyBallotType> styles = PartyBallotTypeQueries.matching(a, PoliticalParty.DEM);
    Assert.assertEquals(new Object(), styles);
  }
  
  /**
   * 
   * @description <description>
   * @explanation <explanation>
   */
  @Ignore
  @Test
  public void testAssemble() {
    final County a = CountyQueries.fromString("4");
    final Set<PartyBallotType> styles = PartyBallotTypeQueries.assemble(a);  
    Assert.assertEquals(new Object(), styles);
  }
  
}
