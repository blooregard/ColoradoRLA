
package us.freeandfair.corla.model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * A suite of tests to verify that the PoliticalParty Enum can find political
 * parties participating in a primary election.
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class PoliticalPartyTest {

  /**
   * Test case to verify valueOf finds the correct political party
   */
  @Test
  public void testLookUp() {
    final PoliticalParty rep = PoliticalParty.valueOf("REP");
    assertEquals("Find Republican party", rep, PoliticalParty.REP);
  }

  /**
   * Test case to verify a case insensitive valueOf finds the correct political
   * party
   */
  @Test
  public void testCaseInsensitiveValueOf() {
    final PoliticalParty dem = PoliticalParty.ignoreCaseValueOf("dem");
    assertEquals("Find Democratic party", dem, PoliticalParty.DEM);
  }

  /**
   * Test case to verify contains confirms the existence of a particular
   * political party
   */
  @Test
  public void testContains() {
    assertTrue("ContainUnity party", PoliticalParty.contains("uni"));
    assertFalse("Does not contain Test party", PoliticalParty.contains("test"));
    assertFalse("Can handle empty string lookup", PoliticalParty.contains(""));
    assertFalse("Can handle null lookup", PoliticalParty.contains(null));
  }
}
