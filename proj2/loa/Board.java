/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Zachary Zhu
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        // FIXME

        // Set contents of _board to board's squares. Initialize _turn and _moveLimit
        for (int i = 0; i < contents.length; i += 1) {
            for (int j = 0; j < contents[i].length; j += 1) {
                Square sq = sq(i, j);
                Piece toPiece = contents[j][i];
                set(sq, toPiece);
            }
        }
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;

    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        // FIXME
        // Set the contents of _board to board's squares
        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < 8; j += 1) {
                Square sq = sq(i, j);
                set(sq, board.get(sq));
            }
        }
        _turn = board.turn();

    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        // FIXME
        if (next != null) {
            _turn = next;
        }
        _board[sq.index()] = v;
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     *  is false. */
    void makeMove(Move move) {
        assert isLegal(move);
        // FIXME

        // Setting TO square to FROM value. FROM is now empty
        Square from = move.getFrom();
        Square to = move.getTo();
        Piece fromVal = get(from);
        Piece toVal = get(to);
        if (fromVal != EMP && fromVal == toVal.opposite()) {
            move = Move.mv(from, to, true);
        }
        _moves.add(move);
        /**
         * Is this necessary?
         * Also, why can we assume that isCapture() is false? Will it be implemented elsewhere?
        if (toVal != EMP) {
            set(to, EMP);
        }
         */
        set(to, fromVal);
        set(from, EMP);
        _turn = _turn.opposite();
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        // FIXME

        // Getting the prev, after, and values there
        Move lastMove = _moves.remove(_moves.size() - 1);
        Square prev = lastMove.getFrom();
        Square after = lastMove.getTo();
        Piece afterValue = get(after);

        // Revert depending on whether or not it's a capture move--do I need to account for captures?
        if (lastMove.isCapture()) {
            set(prev, afterValue);
            set(after, afterValue.opposite());
            _turn = afterValue;
        } else {
            set(prev, afterValue);
            set(after, EMP);
            _turn = afterValue;
        }

    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        // FIXME

        // Wrong direction? Return false
        if (!from.isValidMove(to)) {
            return false;
        }

        // Is blocked? return false -- make sure to check ending squares in blocked
        if (blocked(from, to)) {
            return false;
        }

        // Check to see if it's the right distance in terms of # of pieces
        int distance = from.distance(to);
        int direction = from.direction(to);
        if (distance == 0 || distance != piecesInPath(from, direction)) {
            return false;
        }

        // Otherwise, return true
        return true;
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        // FIXME

        /** Approach: Loop through all the possible boards twice.
         * If I find a legal move between the two squares and it's
         * the right turn, then add it to the possible legal moves.
         */
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < 8; j += 1) {
                for (int k = 0; k < 8; k += 1) {
                    for (int l = 0; l < 8; l += 1) {
                        Square sq1 = sq(i, j);
                        Square sq2 = sq(k, l);
                        if (isLegal(sq1, sq2) && _turn == get(sq1)) {
                            legalMoves.add(Move.mv(sq1, sq2));
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are contiguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            // Tie game
            if (!piecesContiguous(BP) && !piecesContiguous(WP) && movesMade() >= _moveLimit) {
                _winnerKnown = true;
                _winner = EMP;

            // Winner when both are contiguous
            } else if (piecesContiguous(BP) && piecesContiguous(WP)) {
                _winnerKnown = true;
                _winner = _turn.opposite();

            // Black alone is contiguous
            } else if (piecesContiguous(BP)) {
                _winnerKnown = true;
                _winner = BP;

            // White alone is contiguous
            } else if (piecesContiguous(WP)) {
                _winnerKnown = true;
                _winner = WP;

            // Nobody won yet
            } else {
                _winnerKnown = false;
                _winner = null;
            }
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());

            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /**
     * Return the number of paths in direction from Square sq.
     * @param sq for current square
     * @param direction for direction piece wants to go in
     * @return int representing number of pieces in direction from Square sq.
     */
    private int piecesInPath(Square sq, int direction) {
        int total = 1;
        for (int i = 1; i < 8; i += 1) {
            Square dest = sq.moveDest(direction, i);
            if (dest != null && get(dest) != EMP) {
                total += 1;
            }
        }
        int secondDirection;
        if (direction <= 3) {
            secondDirection = direction + 4;
        } else {
            secondDirection = direction - 4;
        }
        for (int i = 1; i < 8; i += 1) {
            Square dest = sq.moveDest(secondDirection, i);
            if (dest != null && get(dest) != EMP) {
                total += 1;
            }
        }
        return total;
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        // FIXME

        // TO square is friendly
        if (get(to) == get(from)) {
            return true;
        }

        // Any square from FROM before TO is ENEMY
        int direction = from.direction(to);
        int distance = from.distance(to);
        for (int i = 1; i < distance; i += 1) {
            Square sq = from.moveDest(direction, i);
            if (get(sq) == get(from).opposite()) {
                return true;
            }
        }

        // Not blocked
        return false;

    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        // FIXME

        // Note to self: does the visited adjust as desired?
        int total = 0;
        if (get(sq) != p) {
            return 0;
        } else {
            int row = sq.row();
            int col = sq.col();
            if (!visited[row][col]) {
                total += 1;
                visited[row][col] = true;
                Square[] adjacentSquares = sq.adjacent();
                for (int i = 0; i < adjacentSquares.length; i += 1) {
                    Square tempSq = adjacentSquares[i];
                    total += numContig(tempSq, visited, p);
                }
            }
        }
        return total;
    }



    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        /**
        if (_subsetsInitialized) {
            return;
        }
         **/
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();

        // FIXME

        boolean[][] visited = new boolean[8][8];
        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < 8; j += 1) {
                Square tempSq = sq(i, j);
                Piece tempSqPiece = get(tempSq);
                if (!visited[j][i] &&  tempSqPiece != EMP) {
                    int regionSize = numContig(tempSq, visited, tempSqPiece);
                    if (tempSqPiece == WP) {
                        _whiteRegionSizes.add(regionSize);
                    } else {
                        _blackRegionSizes.add(regionSize);
                    }
                }
            }
        }
        // And what does this do??
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    // FIXME: Other methods, variables?


    // FIXME: I added this method
    /** Method for getting the current pieces on the board.
     *
     * @return the current pieces on the board
     */
    Piece[] getBoard() {
        return _board;
    }

    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of contiguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
