package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.ArrayList;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Zachary Zhu
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }


    Permutation p1 = new Permutation("(BACD)",
            new Alphabet("ABCD"));
    Permutation p2 = new Permutation("(AB) (CEF)",
            new Alphabet("ABCEFGW"));
    Permutation p3 = new Permutation("(A) (CWE)",
            new Alphabet("ABCEFGW"));
    Permutation p4 = new Permutation("(CEB) (DA) (F) (G) (PRT)",
            new Alphabet("FDABPRTYCEGNMZ"));
    Permutation p5 = new Permutation("",
            new Alphabet("DEFGA"));
    Permutation p6 = new Permutation("",
            new Alphabet("A"));
    Permutation p7 = new Permutation("(AF) (DB) (EC)",
            new Alphabet("ABCDEF"));
    Permutation p8 = new Permutation("(A) (B) (C)",
            new Alphabet("ABC"));


    @Test
    public void constructorTest() {
        ArrayList<String> pTest1 = new ArrayList<String>();
        pTest1.add("BACD");
        assertTrue(p1.getPermutations().equals(pTest1));

        ArrayList<String> pTest2 = new ArrayList<String>();
        pTest2.add("AB"); pTest2.add("CEF");
        pTest2.add("G"); pTest2.add("W");
        assertTrue(p2.getPermutations().equals(pTest2));

        ArrayList<String> pTest3 = new ArrayList<String>();
        pTest3.add("A"); pTest3.add("CWE");
        pTest3.add("B"); pTest3.add("F"); pTest3.add("G");
        assertTrue(p3.getPermutations().equals(pTest3));

        ArrayList<String> pTest8 = new ArrayList<String>();
        pTest8.add("A"); pTest8.add("B"); pTest8.add("C");
        assertTrue(p8.getPermutations().equals(pTest8));

    }


    @Test
    public void checkPermuteTest() {
        assertEquals('A', p1.permute('B'));
        assertEquals('C', p1.permute('A'));
        assertEquals('D', p1.permute('C'));
        assertEquals('B', p1.permute('D'));
        assertEquals(2, p1.permute(0));
        assertEquals(0, p1.permute(1));
        assertEquals(3, p1.permute(2));
        assertEquals(1, p1.permute(3));

        assertEquals('W', p2.permute('W'));
        assertEquals('B', p2.permute('A'));
        assertEquals('E', p2.permute('C'));
        assertEquals('F', p2.permute('E'));
        assertEquals('C', p2.permute('F'));
        assertEquals(6, p2.permute(6));
        assertEquals(1, p2.permute(0));
        assertEquals(0, p2.permute(1));
        assertEquals(3, p2.permute(2));
        assertEquals(0, p2.permute(8));
        assertEquals(3, p2.permute(16));

        assertEquals('W', p3.permute('C'));
        assertEquals('A', p3.permute('A'));
        assertEquals('E', p3.permute('W'));
        assertEquals('C', p3.permute('E'));
        assertEquals('F', p3.permute('F'));

        assertEquals('E', p4.permute('C'));
        assertEquals('B', p4.permute('E'));
        assertEquals('C', p4.permute('B'));
        assertEquals('A', p4.permute('D'));
        assertEquals('D', p4.permute('A'));
        assertEquals(0, p4.permute(0));
        assertEquals(2, p4.permute(1));
        assertEquals(1, p4.permute(2));
        assertEquals(12, p4.permute(12));

    }

    @Test
    public void checkInverseTest() {
        assertEquals('B', p1.invert('A'));
        assertEquals('A', p1.invert('C'));
        assertEquals('C', p1.invert('D'));
        assertEquals('D', p1.invert('B'));
        assertEquals(0, p1.invert(2));
        assertEquals(1, p1.invert(0));
        assertEquals(2, p1.invert(3));
        assertEquals(3, p1.invert(1));

        assertEquals('W', p2.invert('W'));
        assertEquals('A', p2.invert('B'));
        assertEquals('C', p2.invert('E'));
        assertEquals('E', p2.invert('F'));
        assertEquals('F', p2.invert('C'));
        assertEquals(6, p2.invert(6));
        assertEquals(0, p2.invert(1));
        assertEquals(1, p2.invert(0));
        assertEquals(2, p2.invert(3));
        assertEquals(1, p2.invert(7));
        assertEquals(2, p2.invert(17));

        assertEquals('C', p3.invert('W'));
        assertEquals('A', p3.invert('A'));
        assertEquals('W', p3.invert('E'));
        assertEquals('E', p3.invert('C'));
        assertEquals('F', p3.invert('F'));

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
        assertEquals(0, p4.invert(0));
        assertEquals(1, p4.invert(2));
        assertEquals(2, p4.invert(1));
        assertEquals(12, p4.invert(12));

    }

    @Test
    public void checkDerangementTest() {
        assertTrue(p1.derangement());
        assertFalse(p2.derangement());
        assertFalse(p3.derangement());
        assertTrue(p7.derangement());
        assertFalse(p8.derangement());
    }

    @Test
    public void checkSizeTest() {
        assertEquals(4, p1.size());
        assertEquals(7, p2.size());
        assertEquals(7, p3.size());
    }



    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet1() {
        p1.invert('F');
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet2() {
        p2.permute('N');
    }

}
