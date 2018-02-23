package us.freeandfair.corla.query;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.freeandfair.corla.model.County;
import us.freeandfair.corla.model.PartyBallotType;
import us.freeandfair.corla.model.PoliticalParty;
import us.freeandfair.corla.persistence.Persistence;
import us.freeandfair.corla.testutil.HibernateDbUnitTestCase;

public class PartyBallotTypeQueriesTest {
  private Session session = null;

 // @Test
  public void testMatching() {
    final County a = CountyQueries.fromString("1");
    final List<PartyBallotType> styles = PartyBallotTypeQueries.matching(a, PoliticalParty.DEM);
  }
  
  //@Test
  public void testAssemble() {
    final County a = CountyQueries.fromString("4");
    final Set<PartyBallotType> styles = PartyBallotTypeQueries.assemble(a);  
  }
  
}
