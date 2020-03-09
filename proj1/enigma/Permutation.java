package enigma;
import java.util.ArrayList;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Zachary Zhu
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _permutations = parser(cycles);
        if (!clean(_permutations)) {
            throw new EnigmaException("Input contains repeat, "
                    + "empty cycle, or forbidden char");
        }
        addRemainder(_permutations);
    }

    /** Returns an ArrayList of Strings that represent permutations.
     * Raise an exception if input is invalid.
     * @param s as input string
     * @return String ArrayList with given cycles as strings in ArrayList
     */
    public ArrayList<String> parser(String s) {
        if (s.length() == 0) {
            return new ArrayList<String>();
        } else if (s.charAt(s.length() - 1) != ')') {
            throw new EnigmaException("Invalid input: "
                    + "doesn't end with paranthese");
        } else if (s.charAt(0) != '(') {
            throw new EnigmaException("Invalid input: "
                    + "doesn't start with paranthese");
        }
        ArrayList<String> output = new ArrayList<String>();
        int counter = 1;
        boolean openParan = true;
        boolean closeParan = false;
        String temp = "";
        while (counter < s.length()) {
            if (!openParan && closeParan) {
                if (s.charAt(counter) != ' ' && s.charAt(counter) != '(') {
                    throw new EnigmaException("Invalid input format");
                } else if (s.charAt(counter) == '(') {
                    openParan = true; closeParan = false;
                }

            } else if (openParan && !closeParan) {
                if (s.charAt(counter) != ')') {
                    temp += s.charAt(counter);
                } else if (s.charAt(counter) == ')') {
                    output.add(temp); temp = "";
                    closeParan = true; openParan = false;
                }
            }
            counter += 1;
        }
        return output;
    }



    /** Method ensures that no faulty input exists in ArrayList parsed.
     * @param inp an ArrayList
     * @return whether the ArrayList is clean
     *
     */
    public boolean clean(ArrayList<String> inp) {
        String cleaned = "";
        for (String s: inp) {
            if (s.length() == 0) {
                return false;
            }
            cleaned += s;
        }
        for (int i = 0; i < cleaned.length(); i += 1) {
            if (!_alphabet.contains(cleaned.charAt(i))) {
                return false;
            } else if (cleaned.charAt(i) == '(' || cleaned.charAt(i) == ')'
                    || cleaned.charAt(i) == '*' || cleaned.charAt(i) == ' ') {
                return false;
            }
        }
        for (int i = 0; i < cleaned.length(); i += 1) {
            for (int j = i + 1; j < cleaned.length(); j += 1) {
                if (cleaned.charAt(i) == cleaned.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Method to add any characters in alphabet not in a cycle
     * to the permutations.
     * @param input as an ArrayList
     */
    public void addRemainder(ArrayList<String> input) {
        String chars = "";
        for (int i = 0; i < input.size(); i += 1) {
            chars += input.get(i);
        }
        String alpha = _alphabet.getAlphabet();
        for (int i = 0; i < alpha.length(); i += 1) {
            if (chars.indexOf(alpha.charAt(i)) == -1) {
                input.add(Character.toString(alpha.charAt(i)));
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {

    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char c = _alphabet.getAlphabet().charAt(wrap(p));
        char permuted = permute(c);
        return _alphabet.getAlphabet().indexOf(permuted);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char c1 = _alphabet.getAlphabet().charAt(wrap(c));
        char inverted = invert(c1);
        return _alphabet.getAlphabet().indexOf(inverted);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        for (int i = 0; i < _permutations.size(); i += 1) {
            if (_permutations.get(i).indexOf(p) != -1) {
                if (_permutations.get(i).length() == 1) {
                    return p;
                } else {
                    int index = (_permutations.get(i).indexOf(p) + 1);
                    if (index == _permutations.get(i).length()) {
                        index = 0;
                    }
                    return _permutations.get(i).charAt(index);
                }
            }
        }
        throw new EnigmaException("char not found");
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (int i = 0; i < _permutations.size(); i += 1) {
            if (_permutations.get(i).indexOf(c) != -1) {
                if (_permutations.get(i).length() == 1) {
                    return c;
                } else {
                    int index = _permutations.get(i).indexOf(c) - 1;
                    if (index == -1) {
                        index = _permutations.get(i).length() - 1;
                    }
                    return _permutations.get(i).charAt(index);
                }
            }
        }
        throw new EnigmaException("char not found");
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _permutations.size(); i += 1) {
            if (_permutations.get(i).length() == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Shift the alphabet in such a way that it start with character c.
     * @param c represents the starting character
     */
    void shiftAlphabet(char c) {
        int index = _alphabet.getAlphabet().indexOf(c);
        String newPost = _alphabet.getAlphabet().substring(0, index);
        String newPrior = _alphabet.getAlphabet().
                substring(index, _alphabet.size());
        _alphabet = new Alphabet(newPrior + newPost);
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Tracking the permutations. */
    private ArrayList<String> _permutations;

    /** Method to get the permutations.
     * @return _permutations the arraylist of strings
     *  **/
    ArrayList<String> getPermutations() {
        return _permutations;
    }
    /**
     * RINGSTALLONE
     * - if Ringstallone (LAST COMPONENT OF SETTING UP MACHINE):
     *      --> Shift the alphabets of every rotor
     *      --> Settings of the rotor?
     *      --> Fix the settings
     */
}
