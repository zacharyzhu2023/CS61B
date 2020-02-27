package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation constructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    // FIXME: Add tests here that pass on a correct Permutation and fail on buggy Permutations.

    @Test
    public void checkInverseTest() {
        Permutation p1 = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', p1.invert('A'));
        assertEquals('A', p1.invert('C'));
        assertEquals('C', p1.invert('D'));
        assertEquals('D', p1.invert('B'));
        assertEquals('C', p1.invert((int)'D'));
        assertEquals('D', p1.invert((int)'B'));

        Permutation p2 = getNewPermutation("(AB) (CEF)", getNewAlphabet("ABCEFGW"));
        assertEquals('W', p2.invert('W'));
        assertEquals('A', p2.invert('B'));
        assertEquals('C', p2.invert('E'));
        assertEquals('E', p2.invert('F'));
        assertEquals('F', p2.invert('C'));
        assertEquals('E', p2.invert((int)'F'));
        assertEquals('F', p2.invert((int)'C'));

        Permutation p3 = getNewPermutation("(A) (CWE)", getNewAlphabet("ABCEFGW"));
        assertEquals('C', p3.invert('W'));
        assertEquals('A', p3.invert('A'));
        assertEquals('W', p3.invert('E'));
        assertEquals('E', p3.invert('C'));
        assertEquals('F', p3.invert('F'));
        assertEquals('E', p3.invert((int)'C'));
        assertEquals('F', p3.invert((int)'F'));

        Permutation p4 = getNewPermutation("(CEB) (DA) (F) (G) (PRT)", getNewAlphabet("FDABPRTYCEGNMZ"));
        assertEquals('C', p4.invert('E'));
        assertEquals('E', p4.invert('B'));
        assertEquals('B', p4.invert('C'));
        assertEquals('D', p4.invert('A'));
        assertEquals('A', p4.invert('D'));
        assertEquals('F', p4.invert('F'));
        assertEquals('G', p4.invert('G'));
        assertEquals('P', p4.invert('R'));
        assertEquals('R', p4.invert('T'));
        assertEquals('T', p4.invert('P'));
        assertEquals('M', p4.invert('M'));
        assertEquals('Z', p4.invert('Z'));
        assertEquals('P', p4.invert((int)'R'));
        assertEquals('R', p4.invert((int)'T'));
        assertEquals('T', p4.invert((int)'P'));

    }

    @Test
    public void checkPermuteTest() {
        Permutation p1 = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('A', p1.permute('B'));
        assertEquals('C', p1.permute('A'));
        assertEquals('D', p1.permute('C'));
        assertEquals('B', p1.permute('D'));
        assertEquals('D', p1.permute((int)'C'));
        assertEquals('B', p1.permute((int)'D'));

        Permutation p2 = getNewPermutation("(AB) (CEF)", getNewAlphabet("ABCEFGW"));
        assertEquals('W', p2.permute('W'));
        assertEquals('B', p2.permute('A'));
        assertEquals('E', p2.permute('C'));
        assertEquals('F', p2.permute('E'));
        assertEquals('C', p2.permute('F'));
        assertEquals('F', p2.permute((int)'E'));
        assertEquals('C', p2.permute((int)'F'));

        Permutation p3 = getNewPermutation("(A) (CWE)", getNewAlphabet("ABCEFGW"));
        assertEquals('W', p3.permute('C'));
        assertEquals('A', p3.permute('A'));
        assertEquals('E', p3.permute('W'));
        assertEquals('C', p3.permute('E'));
        assertEquals('F', p3.permute('F'));
        assertEquals('C', p3.permute((int)'E'));
        assertEquals('F', p3.permute((int)'F'));

        Permutation p4 = getNewPermutation("(CEB) (DA) (F) (G) (PRT)", getNewAlphabet("FDABPRTYCEGNMZ"));
        assertEquals('E', p4.permute('C'));
        assertEquals('B', p4.permute('E'));
        assertEquals('C', p4.permute('B'));
        assertEquals('A', p4.permute('D'));
        assertEquals('D', p4.permute('A'));
        assertEquals('C', p4.permute((int)'B'));
        assertEquals('A', p4.permute((int)'D'));
        assertEquals('D', p4.permute((int)'A'));
    }

    @Test
    public void checkDerangementTest() {
        Permutation p1 = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertTrue(p1.derangement());
        Permutation p2 = getNewPermutation("(AB) (CEF)", getNewAlphabet("ABCEFGW"));
        assertFalse(p2.derangement());
        Permutation p3 = getNewPermutation("(A) (CWE)", getNewAlphabet("ABCEFGW"));
        assertFalse(p3.derangement());
    }

    @Test
    public void checkSizeTest() {
        Permutation p1 = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(4, p1.size());
        Permutation p2 = getNewPermutation("(AB) (CEF)", getNewAlphabet("ABCEFGW"));
        assertEquals(7, p2.size());
        Permutation p3 = getNewPermutation("(A) (CWE)", getNewAlphabet("ABCEFGW"));
        assertEquals(7, p3.size());
    }

    @Test
    public void checkAlphabetTest() {
        Alphabet a1 = getNewAlphabet("ABCD");
        Permutation p1 = getNewPermutation("(BACD)", a1);
        assertEquals(p1.alphabet(), a1);
        Alphabet a2 = getNewAlphabet("ABCEFGW");
        Permutation p2 = getNewPermutation("(AB) (CEF)", a2);
        assertEquals(p2.alphabet(), a2);
        Alphabet a3 = getNewAlphabet("ABCEFGW");
        Permutation p3 = getNewPermutation("(A) (CWE)", a3);
        assertEquals(p3.alphabet(), a3);
    }
}
