
package us.freeandfair.corla.model;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * A suite of tests to verify that the PoliticalParty Enum can
 * find political parties participating in a primary election.
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class PoliticalPartyTest {

  /**
   * Test case to verify valueOf finds the correct political party
   */
  @Test
  public void testLookUp() {
    final PoliticalParty rep = PoliticalParty.valueOf("REP");
    Assert.assertEquals(rep, PoliticalParty.REP);
  }

  /**
   * Test case to verify a case insensitive valueOf finds the correct 
   * political party
   */
  @Test
  public void testCaseInsensitiveValueOf() {
    final PoliticalParty dem = PoliticalParty.ignoreCaseValueOf("dem");
    Assert.assertEquals(dem, PoliticalParty.DEM);
  }

  /**
   * Test case to verify contains confirms the existence of a particular 
   * political party
   */
  @Test
  public void testContains() {
    Assert.assertTrue(PoliticalParty.contains("uni"));
    Assert.assertFalse(PoliticalParty.contains("test"));
    Assert.assertFalse(PoliticalParty.contains(""));
    Assert.assertFalse(PoliticalParty.contains(null));
  }
}
