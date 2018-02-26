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
 * The election types.
 * 
 * @author Ben Rector <ben.rector@sos.state.co.us>
 * @version 1.0.0
 */
public enum ElectionType {
  coordinated("Coordinated"),
  general("General"),
  primary("Primary"), 
  recall("Recall");

  /**
   * The pretty printing string for this enum value.
   */
  private final String my_pretty_string;

  /**
   * Constructs a new election type.
   * 
   * @param the_pretty_string The pretty printing string.
   */
  private ElectionType(final String the_pretty_string) {
    my_pretty_string = the_pretty_string;
  }
  
  /**
   * 
   */
  public static boolean contains(final String value) {
    boolean found = false;

    for (final ElectionType e : ElectionType.values()) {
      if (e.name().equalsIgnoreCase(value)) {
        found = true;
        break;
      }
    }

    return found;
  }

  /**
   * Convenience pass through class method
   */
  public static ElectionType ignoreCaseValueOf(final String name) {
    return Enum.valueOf(ElectionType.class, name.toUpperCase(Locale.getDefault()));
  }

  /**
   * @return the pretty printing string for this enum value.
   */
  public String prettyString() {
    return my_pretty_string;
  }

}
