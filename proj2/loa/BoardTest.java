/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

import static loa.Piece.*;
import static loa.Square.sq;
import static loa.Move.mv;

/** Tests of the Board class API.
 *  @author Zachary Zhu
 */
public class BoardTest {

    /** A "general" position. */
    static final Piece[][] BOARD1 = {
        { EMP, BP,  EMP,  BP,  BP, EMP, EMP, EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP,  BP,  BP, EMP, WP  },
        { WP,  EMP,  BP, EMP, EMP,  WP, EMP, EMP  },
        { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP  },
        { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP  },
        { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD2 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  BP,  BP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  BP,  WP,  WP, EMP,  WP },
        { EMP,  WP,  WP,  BP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP,  BP, EMP, EMP, EMP, EMP },
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD3 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  WP, EMP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  BP,  WP,  WP,  WP, EMP },
        { EMP,  WP,  WP,  WP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };


    static final String BOARD1_STRING =
        "===\n"
        + "    - b b b - b b - \n"
        + "    - - - - - - - - \n"
        + "    w - - - b - - w \n"
        + "    w - w w - w - - \n"
        + "    w - b - - w - - \n"
        + "    w - - - b b - w \n"
        + "    w - - - - - - w \n"
        + "    - b - b b - - - \n"
        + "Next move: black\n"
        + "===";

    /** Test display */
    @Test
    public void toStringTest() {
        assertEquals(BOARD1_STRING, new Board(BOARD1, BP).toString());
    }

    /** Test legal moves. */
    @Test
    public void testLegality1() {
        Board b = new Board(BOARD1, BP);
        assertTrue("f3-d5", b.isLegal(mv("f3-d5")));
        assertTrue("f3-h5", b.isLegal(mv("f3-h5")));
        assertTrue("f3-h1", b.isLegal(mv("f3-h1")));
        assertTrue("f3-b3", b.isLegal(mv("f3-b3")));
        assertFalse("f3-d1", b.isLegal(mv("f3-d1")));
        assertFalse("f3-h3", b.isLegal(mv("f3-h3")));
        assertFalse("f3-e4", b.isLegal(mv("f3-e4")));
        assertFalse("c4-c7", b.isLegal(mv("c4-c7")));
        assertFalse("b1-b4", b.isLegal(mv("b1-b4")));
    }

    /** Test contiguity. */
    @Test
    public void testContiguous1() {
        Board b1 = new Board(BOARD1, BP);
        assertFalse("Board 1 black contiguous?", b1.piecesContiguous(BP));
        assertFalse("Board 1 white contiguous?", b1.piecesContiguous(WP));
        assertFalse("Board 1 game over?", b1.gameOver());
        Board b2 = new Board(BOARD2, BP);
        assertTrue("Board 2 black contiguous?", b2.piecesContiguous(BP));
        assertFalse("Board 2 white contiguous?", b2.piecesContiguous(WP));
        assertTrue("Board 2 game over", b2.gameOver());
        Board b3 = new Board(BOARD3, BP);
        assertTrue("Board 3 white contiguous?", b3.piecesContiguous(WP));
        assertTrue("Board 3 black contiguous?", b3.piecesContiguous(WP));
        assertTrue("Board 3 game over", b2.gameOver());
    }

    @Test
    public void testEquals1() {
        Board b1 = new Board(BOARD1, BP);
        Board b2 = new Board(BOARD1, BP);

        assertEquals("Board 1 equals Board 1", b1, b2);
    }

    @Test
    public void testMove1() {
        Board b0 = new Board(BOARD1, BP);
        Board b1 = new Board(BOARD1, BP);
        b1.makeMove(mv("f3-d5"));
        assertEquals("square d5 after f3-d5", BP, b1.get(sq(3, 4)));
        assertEquals("square f3 after f3-d5", EMP, b1.get(sq(5, 2)));
        assertEquals("Check move count for board 1 after one move",
                     1, b1.movesMade());
        b1.retract();
        assertEquals("Check for board 1 restored after retraction", b0, b1);
        assertEquals("Check move count for board 1 after move + retraction",
                     0, b1.movesMade());
    }

    @Test
    public void testLegalMoves() {
        Board b1 = new Board(BOARD1, BP);
        System.out.println(b1.toString());
        List<Move> legalMoves1 = b1.legalMoves();
        for (int i = 0; i < legalMoves1.size(); i += 1) {
            System.out.println(legalMoves1.get(i).toString());
        }

        Board b2 = new Board(BOARD1, WP);
        System.out.println(b2.toString());
        List<Move> legalMoves2 = b2.legalMoves();
        for (int i = 0; i < legalMoves2.size(); i += 1) {
            System.out.println(legalMoves2.get(i).toString());
        }

        Board b3 = new Board(BOARD2, BP);
        System.out.println(b3.toString());
        List<Move> legalMoves3 = b3.legalMoves();
        for (int i = 0; i < legalMoves3.size(); i += 1) {
            System.out.println(legalMoves3.get(i).toString());
        }

        Board b4 = new Board(Board.INITIAL_PIECES, BP);
        System.out.println(b4.toString());
        List<Move> legalMoves4 = b4.legalMoves();
        for (int i = 0; i < legalMoves4.size(); i += 1) {
            System.out.println(legalMoves4.get(i).toString());
        }
    }

    @Test
    public void getTest() {
        Board b0 = new Board(BOARD1, BP);
        Square sq0 = sq(0, 0);
        Square sq1 = sq(1, 1);
        Square sq2 = sq(2, 2);
        Square sq3 = sq(5, 4);
        assertEquals(b0.get(sq0), EMP);
        assertEquals(b0.get(sq1), EMP);
        assertEquals(b0.get(sq2), EMP);
        assertEquals(b0.get(sq3), WP);
    }

    @Test
    public void setTest() {
        Board b0 = new Board(BOARD1, BP);
        Square sq0 = sq(0, 0);
        Square sq1 = sq(1, 1);
        Square sq2 = sq(2, 2);
        Square sq3 = sq(5, 4);
        b0.set(sq0, BP);
        b0.set(sq1, WP);
        b0.set(sq2, EMP);
        b0.set(sq3, BP);
        assertEquals(b0.get(sq0), BP);
        assertEquals(b0.get(sq1), WP);
        assertEquals(b0.get(sq2), EMP);
        assertEquals(b0.get(sq3), BP);
    }

    @Test
    public void testGameOver() {
        Board b0 = new Board(BOARD1, BP);
        Board b1 = new Board(BOARD2, WP);
        Board b2 = new Board(BOARD3, BP);
        assertFalse(b0.gameOver());
        assertTrue(b1.gameOver());
        assertTrue(b2.gameOver());
    }

    @Test
    public void testWinner() {
        Board b0 = new Board(BOARD1, BP);
        Board b1 = new Board(BOARD2, WP);
        Board b2 = new Board(BOARD3, BP);
        assertEquals(b0.winner(), null);
        assertEquals(b1.winner(), BP);
        assertEquals(b2.winner(), WP);
    }

    @Test
    public void testMovesMade() {
        Board b0 = new Board(BOARD1, BP);
        b0.makeMove(mv("b1-b3"));
        b0.makeMove(mv("a2-a7"));
        b0.makeMove(mv("b3-g3"));
        assertEquals(b0.movesMade(), 3);
        Board b1 = new Board(BOARD2, WP);
        b1.makeMove(mv("d5-a2"));
        b1.makeMove(mv("b4-b1"));
        assertEquals(b1.movesMade(), 2);
    }

    @Test
    public void retractTest() {
        Board b0 = new Board(BOARD1, BP);
        b0.makeMove(mv("b1-b3"));
        b0.retract();
        Board b00 = new Board(BOARD1, BP);
        assertTrue(b0.toString().equals(b00.toString()));
        assertEquals(b0.movesMade(), 0);
        b0.makeMove(mv("b1-b3"));
        b0.makeMove(mv("a2-a7"));
        b0.retract();
        b00.makeMove(mv("b1-b3"));
        assertTrue(b0.toString().equals(b00.toString()));
        assertEquals(b0.movesMade(), 1);
    }

    @Test
    public void clearTest() {
        Board b0 = new Board(BOARD1, BP);
        b0.makeMove(mv("b1-b3"));
        b0.makeMove(mv("a2-a7"));
        b0.makeMove(mv("b3-g3"));
        b0.clear();
        Board b00 = new Board(Board.INITIAL_PIECES, BP);
        assertTrue(b0.toString().equals(b00.toString()));
    }

    @Test
    public void getRegionSizes() {
        Board b0 = new Board(BOARD1, BP);
        List<Integer> regionSizesBP0 = b0.getRegionSizes(BP);
        List<Integer> regionSizesWP0 = b0.getRegionSizes(WP);
        String sizeBP0 = "";
        for (int i: regionSizesBP0) {
            sizeBP0 += i + ", ";
        }
        System.out.println(sizeBP0);
        assertTrue(sizeBP0.equals("3, 2, 2, 2, 1, 1, 1, "));
        String sizeWP0 = "";
        for (int i: regionSizesWP0) {
            sizeWP0 += i + ", ";
        }
        assertTrue(sizeWP0.equals("5, 2, 2, 2, 1, "));

        Board b1 = new Board(BOARD3, BP);
        List<Integer> regionSizesBP1 = b1.getRegionSizes(BP);
        List<Integer> regionSizesWP1 = b1.getRegionSizes(WP);
        String sizeBP1 = "";
        for (int i: regionSizesBP1) {
            sizeBP1 += i + ", ";
        }
        assertTrue(sizeBP1.equals("5, "));
        String sizeWP1 = "";
        for (int i: regionSizesWP1) {
            sizeWP1 += i + ", ";
        }
        assertTrue(sizeWP1.equals("11, "));


    }
}
