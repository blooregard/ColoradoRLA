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

import java.util.Locale;

/**
 * The political parties which can participate in a primary election.
 * 
 * @author Ben Rector <ben.rector@sos.state.co.us>
 * @version 1.0.0
 */
public enum PoliticalParty {
  DEM("Democratic"), 
  REP("Republican"), 
  ACN("American Constitution"), 
  GRN("Green"), 
  LBR("Libertarian"), 
  UNI("Unity");

  /**
   * The pretty printing string for this enum value.
   */
  private final String my_pretty_string;

  /**
   * Constructs a new PoliticalParty.
   * 
   * @param the_pretty_string The pretty printing string.
   */
  PoliticalParty(final String the_pretty_string) {
    my_pretty_string = the_pretty_string;
  }
  
  /**
   * 
   */
  public static boolean contains(final String value) {
    boolean found = false;

    for (final PoliticalParty p : PoliticalParty.values()) {
      if (p.name().equalsIgnoreCase(value)) {
        found = true;
        break;
      }
    }

    return found;
  }

  /**
   * Convenience pass through class method
   */
  public static PoliticalParty ignoreCaseValueOf(final String name) {
    return Enum.valueOf(PoliticalParty.class, name.toUpperCase(Locale.getDefault()));
  }

  /**
   * @return the pretty printing string for this enum value.
   */
  public String prettyString() {
    return my_pretty_string;
  }

}
