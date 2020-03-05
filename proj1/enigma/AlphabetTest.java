package enigma;

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

public class AlphabetTest {


    Alphabet a1 = new Alphabet();
    Alphabet a2 = new Alphabet("abcdefg");
    Alphabet a3 = new Alphabet("thebaMN48");
    Alphabet a4 = new Alphabet("tui");
    Alphabet a5 = new Alphabet("t");

    @Test (expected = EnigmaException.class)
    public void faultyConstructor1() {
        Alphabet a15 = new Alphabet("thee");
    }

    @Test (expected = EnigmaException.class)
    public void faultyConstructor2() {
        Alphabet a16 = new Alphabet("authg9msp0bc30");
    }


    @Test
    public void sizeTest() {
        assertEquals(26, a1.size());
        assertEquals(7, a2.size());
        assertEquals(9, a3.size());
        assertEquals(3, a4.size());
        assertEquals(1, a5.size());
    }

    @Test
    public void containsTest() {
        assertTrue(a1.contains('G'));
        assertTrue(a1.contains('K'));
        assertFalse(a1.contains('g'));
        assertFalse(a1.contains('"'));
        assertTrue(a2.contains('a'));
        assertTrue(a2.contains('d'));
        assertFalse(a2.contains('1'));
        assertFalse(a2.contains('q'));
        assertTrue(a3.contains('t'));
        assertTrue(a3.contains('8'));
        assertFalse(a3.contains('1'));
        assertFalse(a3.contains('m'));
        assertTrue(a4.contains('u'));
        assertTrue(a4.contains('i'));
        assertFalse(a4.contains('p'));
        assertFalse(a4.contains('0'));
        assertTrue(a5.contains('t'));
        assertFalse(a5.contains('J'));
    }

    @Test (expected = EnigmaException.class)
    public void faultyToCharTest1() {
        a1.toChar(-1);
    }

    @Test (expected = EnigmaException.class)
    public void faultyToCharTest2() {
        a2.toChar(10);
    }

    @Test
    public void toCharTest() {
        assertEquals('F', a1.toChar(5));
        assertEquals('Z', a1.toChar(25));
        assertEquals('a', a2.toChar(0));
        assertEquals('c', a2.toChar(2));
        assertEquals('h', a3.toChar(1));
        assertEquals('b', a3.toChar(3));
        assertEquals('t', a4.toChar(0));
        assertEquals('u', a4.toChar(1));
        assertEquals('t', a5.toChar(0));
    }

    @Test (expected = EnigmaException.class)
    public void faultyToIntTest1() {
        a1.toInt('t');
    }

    @Test (expected = EnigmaException.class)
    public void faultyToIntTest2() {
        a5.toInt('N');
    }

    @Test
    public void toIntTest() {
        assertEquals(0, a1.toInt('A'));
        assertEquals(0, a2.toInt('a'));
        assertEquals(8, a3.toInt('8'));
        assertEquals(1, a4.toInt('u'));
        assertEquals(0, a5.toInt('t'));
    }

    public static void main(String[] args) {
        System.exit(textui.runClasses(AlphabetTest.class));
    }

}
