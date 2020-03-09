package enigma;
import org.junit.Test;
import static org.junit.Assert.*;
import static enigma.TestUtils.*;

public class RotorTest {

    private String upperCase = UPPER_STRING;
    /** Creation of new rotors and their respective permutations. **/
    Permutation p1 = new Permutation("(BACD)",
            new Alphabet("ABCD"));
    Permutation p2 = new Permutation("(AB) (CEF)",
            new Alphabet("ABCEFGW"));
    Permutation p3 = new Permutation("(A) (CWE)",
            new Alphabet("ABCEFGW"));
    Permutation p4 = new Permutation("(CEB) (DA) "
            + "(F) (G) (PRT)",
            new Alphabet("FDABPRTYCEGNMZ"));
    Permutation p5 = new Permutation("",
            new Alphabet("DEFGA"));
    Permutation p6 = new Permutation("",
            new Alphabet("A"));
    Permutation p7 = new Permutation("(AF) (DB) (EC)",
            new Alphabet("ABCDEF"));
    Permutation p8 = new Permutation("(A) (B) (C)",
            new Alphabet("ABC"));
    Permutation p9 = new Permutation("(01234)   "
            + "(5678) (9AB) (CDEFGH)",
            new Alphabet("0123456789ABCDEFGH"));
    Rotor r1 = new Rotor("r1", p1);
    Rotor r2 = new Rotor("r2", p2);
    Rotor r3 = new FixedRotor("r3", p3);
    Rotor r4 = new FixedRotor("r4", p4);
    Rotor r5 = new Reflector("r5", p5);
    /** Notches at A **/
    Rotor r6  = new MovingRotor("r6", p6, "A");
    /** Notches at ADC **/
    Rotor r7 = new MovingRotor("r7", p7, "ADC");
    /** Notches at B **/
    Rotor r8 = new MovingRotor("r8", p8, "B");
    /** Notches at 05BGH **/
    Rotor r9 = new MovingRotor("r9", p9, "05BGH");


    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages.
     *  I inserted Rotor rotor & String alpha into here.
     *   */

    private void checkRotor(Rotor rotor, String alpha, String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                    ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                    ci, rotor.convertBackward(ei));
        }
    }

    @Test
    public void reflectingTest() {
        assertFalse(r1.reflecting());
        assertFalse(r2.reflecting());
        assertFalse(r3.reflecting());
        assertFalse(r4.reflecting());
        assertTrue(r5.reflecting());
        assertFalse(r6.reflecting());
        assertFalse(r7.reflecting());
        assertFalse(r8.reflecting());
        assertFalse(r9.reflecting());
    }

    @Test
    public void rotatingTest() {
        assertFalse(r1.rotates());
        assertFalse(r2.rotates());
        assertFalse(r3.rotates());
        assertFalse(r4.rotates());
        assertFalse(r5.rotates());
        assertTrue(r6.rotates());
        assertTrue(r7.rotates());
        assertTrue(r8.rotates());
        assertTrue(r9.rotates());

    }

    @Test
    public void setTest() {

    }

    @Test
    public void settingTest() {

    }

    @Test
    public void convertForwardTest() {

    }

    @Test
    public void convertBackwardTest() {

    }

    @Test
    public void advanceTest() {

    }

    public static void main(String[] args) {

    }

}
