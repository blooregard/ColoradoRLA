package us.freeandfair.corla.query;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import us.freeandfair.corla.model.County;
import us.freeandfair.corla.model.PartyBallotType;
import us.freeandfair.corla.model.PoliticalParty;
import us.freeandfair.corla.persistence.Persistence;

public class PartyBallotTypeQueriesTest {
  private Session session = null;

  @BeforeMethod
  public void before() throws FileNotFoundException, IOException {
    final Properties p = new Properties();
    p.load(new FileReader(getClass().getResource("/default.properties").getFile()));
    
    Persistence.setProperties(p);
    session = Persistence.openSession();
  }
  
  @AfterMethod
  public void after() {
    session.close();
  }

  //@Test
  public void matching() {
    final County a = CountyQueries.fromString("1");
    final List<PartyBallotType> styles = PartyBallotTypeQueries.matching(a, PoliticalParty.DEM);

  }
  
  //@Test
  public void assemble() {
    final County a = CountyQueries.fromString("4");
    final Set<PartyBallotType> styles = PartyBallotTypeQueries.assemble(a);
    
    
  }
  
}
