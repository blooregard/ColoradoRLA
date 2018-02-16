/*
 * Free & Fair Colorado RLA System
 * 
 * @title colorado_rla
 * 
 * @created Feb 15, 2018
 * 
 * @copyright 2018 Free & Fair
 * 
 * @license GNU General Public License 3.0
 * 
 * @creator blooregard <ben.rector@gmail.com>
 * 
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import us.freeandfair.corla.model.CVRContestInfo;
import us.freeandfair.corla.model.CastVoteRecord;
import us.freeandfair.corla.model.CastVoteRecord.RecordType;
import us.freeandfair.corla.model.County;
import us.freeandfair.corla.model.PartyBallotType;
import us.freeandfair.corla.model.PoliticalParty;
import us.freeandfair.corla.persistence.Persistence;

/**
 * Queries having to do with CountyContestResult entities.
 * 
 * @author Ben Rector <ben.rector@sos.state.co.us>
 * @version 1.0.0
 */
public final class PartyBallotTypeQueries {

  /**
   * 
   */
  private PartyBallotTypeQueries() {

  }

  /**
   *
   *
   * @param the_county The county.
   * @param the_political_party The political party.
   * @return the matched PartyBallotType list.
   */
  public static List<PartyBallotType> matching(final County the_county,
                                               final PoliticalParty the_political_party) {
    List<PartyBallotType> results = null;

    final Session s = Persistence.currentSession();
    final CriteriaBuilder cb = s.getCriteriaBuilder();
    final CriteriaQuery<PartyBallotType> cq = cb.createQuery(PartyBallotType.class);

    final Root<PartyBallotType> root = cq.from(PartyBallotType.class);
    final List<Predicate> conjuncts = new ArrayList<>();
    conjuncts.add(cb.equal(root.get("my_county_id"), the_county.id()));
    conjuncts.add(cb.equal(root.get("my_political_party"), the_political_party));
    cq.select(root).where(cb.and(conjuncts.toArray(new Predicate[conjuncts.size()])));

    final TypedQuery<PartyBallotType> query = s.createQuery(cq);
    results = query.getResultList();
    return results;
  }

  private static PartyBallotType parse(final CastVoteRecord record) {
    for (final CVRContestInfo info : record.contestInfo()) {
      final String party = info.contest().description();
      if (PoliticalParty.contains(party)) {
        return new PartyBallotType(PoliticalParty.valueOf(party), record.countyID(),
                                   record.ballotType());
      }
    }
    return null;
  }

  /**
   * 
   */
  public static Set<PartyBallotType> assemble(final County the_county) {
    Set<PartyBallotType> styles = new HashSet<PartyBallotType>();
    
    final Stream<CastVoteRecord> cvrStream = 
        CastVoteRecordQueries.getMatching(the_county.id(), RecordType.UPLOADED);
    
    if (cvrStream != null) {
      styles = cvrStream.map(cvr -> parse(cvr)).collect(Collectors.toSet());
    }
        
    return styles;
  }
}
