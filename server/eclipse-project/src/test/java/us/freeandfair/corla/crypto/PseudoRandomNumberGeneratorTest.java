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

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * A test case for the PseudoRandomNumberGenerator.
 * 
 * @author Joey Dodds <jdodds@galois.com>
 * @version 1.0.0
 */
// TestNG classes do not need constructors
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class PseudoRandomNumberGeneratorTest {
  /**
   * A single test case that generates a 47 number sequence.
   */
  @Test()
  public void testRandomGenerator() {
    final String seed = "3546311556112163624615351222";
    final PseudoRandomNumberGenerator gen =
        new PseudoRandomNumberGenerator(seed, false, 1, 876);
    final List<Integer> numbers = gen.getRandomNumbers(0, 46);
    final List<Integer> expected = Arrays
        .asList(740, 180, 264, 789, 238, 448, 272, 611, 761, 208, 596, 88, 160, 113, 766, 427,
                184, 816, 653, 411, 779, 331, 339, 487, 594, 235, 65, 527, 821, 490, 461, 251,
                471, 414, 174, 567, 300, 134, 144, 357, 786, 792, 218, 550, 787, 537, 197);
    Assert.assertEquals(numbers, expected);
  }

  /**
   * A test case to verify the random selection with duplicates allowed
   */
  @Test()
  public void testRandomGeneratorDups() {
    final String seed = "3546311556112163624615351222";
    final PseudoRandomNumberGenerator gen =
        new PseudoRandomNumberGenerator(seed, true, 1, 876);
    final List<Integer> numbers = gen.getRandomNumbers(0, 46);
    numbers.sort((a, b) -> a.compareTo(b));
    final List<Integer> expected = Arrays
        .asList(65, 88, 113, 134, 144, 160, 174, 180, 184, 208, 218, 235, 238, 251, 264, 272,
                300, 331, 339, 357, 411, 414, 427, 448, 461, 471, 487, 490, 527, 537, 550, 567,
                594, 596, 611, 611, 653, 740, 761, 766, 779, 786, 787, 789, 792, 816, 821);
    Assert.assertEquals(numbers, expected);
  }

  /**
   * Test the seed verification class method
   */
  @Test()
  public void testSeedOnlyContainsDigits() {
    Assert.assertFalse(PseudoRandomNumberGenerator.seedOnlyContainsDigits("AAAAAA"));

    Assert.assertTrue(PseudoRandomNumberGenerator.seedOnlyContainsDigits("1234567890"));
  }
}
