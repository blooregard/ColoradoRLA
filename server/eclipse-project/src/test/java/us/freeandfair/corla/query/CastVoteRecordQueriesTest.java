package us.freeandfair.corla.query;

import org.testng.annotations.Test;

import us.freeandfair.corla.model.CastVoteRecord;
import us.freeandfair.corla.model.CastVoteRecord.RecordType;
import us.freeandfair.corla.model.PartyBallotType;
import us.freeandfair.corla.model.PoliticalParty;
import us.freeandfair.corla.persistence.Persistence;

import org.testng.annotations.BeforeClass;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.testng.annotations.AfterClass;

public class CastVoteRecordQueriesTest {
  private Session session = null;

  @BeforeClass
  public void before() throws FileNotFoundException, IOException {
    Properties p = new Properties();
    p.load(new FileReader(getClass().getResource("/default.properties").getFile()));
    
    Persistence.setProperties(p);
    session = Persistence.openSession();
  }
  
  @AfterClass
  public void after() {
    session.close();
  }
  
 

  @Test
  public void getMatching() {
    
  }
}
