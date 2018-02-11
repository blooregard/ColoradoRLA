/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * 
 * @created Jul 26, 2017
 * 
 * @copyright 2017 Colorado Department of State
 * 
 * @license SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * @creator Joey Dodds <jdodds@galois.com>
 * 
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.crypto;

import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.annotations.Test;

/**
 * A test case for the PseudoRandomNumberGenerator.
 *
 * @author Joey Dodds <jdodds@galois.com>
 * @version 1.0.0
 */
// TestNG classes do not need constructors
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class HashCheckerTest {
  /**
   * A single test case that simply hashes a file containing the characters
   * "ColoradoRLA".
   */
  @Test()
  public void testHashing() {
    final File test_file = new File(this.getClass().getResource("/HashFile.txt").getFile());
    assertEquals("F9A25DA7060735572E32FCF72C33EE73476E589F7F02256DAFFB4C618D8F9EA2",
                 HashChecker.hashFile(test_file));
    assertEquals("F9A25DA7060735572E32FCF72C33EE73476E589F7F02256DAFFB4C618D8F9EA2",
                 HashChecker.hashFile(test_file.toString()));
  }
}
