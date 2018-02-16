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

package us.freeandfair.corla.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.Immutable;

import us.freeandfair.corla.persistence.PersistentEntity;

/**
 * A class to represent a relationship between political parties and a ballot
 * type (style).
 * 
 * @author Ben Rector <ben.rector@sos.state.co.us>
 * @version 1.0.0
 */
@Entity
@Immutable // this is a Hibernate-specific annotation, but there is no JPA
           // alternative
@Cacheable(false)
@Table(name = "party_ballot_type", uniqueConstraints = {@UniqueConstraint(columnNames = {
    "political_party", "county_id", "ballot_type"})}, indexes = {
        @Index(name = "idx_pbs_county_ballot_type", columnList = "county_id, ballot_type"),
        @Index(name = "idx_pbs_county_party", columnList = "county_id, political_party")})
public class PartyBallotType implements PersistentEntity, Serializable {

  /**
   * The serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  /**
   * The ID number.
   */
  @Id
  @Column(updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long my_id;

  /**
   * The version (for optimistic locking).
   */
  @Version
  private Long my_version;

  /**
   * The political party associated to the ballot type.
   */
  @Column(name = "political_party", updatable = false, nullable = false)
  @Enumerated(EnumType.STRING)
  private PoliticalParty my_political_party;

  /**
   * The county ID of this cast vote record.
   */
  @Column(name = "county_id", updatable = false, nullable = false)
  private Long my_county_id;

  /**
   * The ballot style of this cast vote record.
   */
  @Column(name = "ballot_type", updatable = false, nullable = false)
  private String my_ballot_type;

  /**
   * Constructs an empty party ballot type, solely for persistence.
   */
  public PartyBallotType() {
    super();
  }

  /**
   * Constructs a new cast vote record.
   * 
   * @param the_record_type The type.
   * @param the_timestamp The timestamp.
   * @param the_county_id The county ID.
   * @param the_cvr_number The CVR number (as imported).
   * @param the_sequence_number The sequence number, if applicable.
   * @param the_scanner_id The scanner ID.
   * @param the_batch_id The batch ID.
   * @param the_record_id The record ID.
   * @param the_imprinted_id The imprinted ID.
   * @param the_ballot_type The ballot type.
   * @param the_choices A map of the choices made in each contest.
   */
  public PartyBallotType(final PoliticalParty the_political_party, final Long the_county_id,
                         final String the_ballot_type) {
    super();
    my_political_party = the_political_party;
    my_county_id = the_county_id;
    my_ballot_type = the_ballot_type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long id() {
    return my_id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setID(final Long the_id) {
    my_id = the_id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long version() {
    return my_version;
  }

  /**
   * Return the political party
   */
  public PoliticalParty politicalParty() {
    return my_political_party;
  }

  /**
   * Return the county
   */
  public Long county() {
    return my_county_id;
  }

  /**
   * Return the ballot type (style)
   */
  public String ballotType() {
    return my_ballot_type;
  }

  /**
   * Create the hashcode of the core attributes
   */
  @Override
  public int hashCode() {
    return Objects.hash(my_id, my_political_party, my_county_id, my_ballot_type);
  }

  /**
   * Evaluate the equivalence of the other object and "this"
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    final PartyBallotType other = (PartyBallotType) obj;
    return Objects.equals(my_political_party, other.my_political_party) &&
           Objects.equals(this.my_county_id, other.my_county_id) &&
           Objects.equals(this.my_ballot_type, other.my_ballot_type);
  }

  /**
   * Stringify this object
   */
  @Override
  public String toString() {
    return "PartyBallotType [my_id=" + my_id + ", my_version=" + my_version +
           ", my_political_party=" + my_political_party + ", my_county=" + my_county_id +
           ", my_ballot_type=" + my_ballot_type + "]";
  }

}
