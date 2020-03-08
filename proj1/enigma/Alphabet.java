package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Zachary Zhu
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        if (chars.length() == 0) {
            throw new EnigmaException("Invalid format: Empty alphabet");
        }
        if (hasDuplicates(chars)) {
            throw new EnigmaException("Alphabet has duplicate characters");
        }
        if (chars.indexOf(' ') != -1) {
            throw new EnigmaException("Alphabet contains whitespace");
        }
        _chars = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.indexOf(ch) != -1;
    }

    /** Returns true if a String has duplicate characters.
     * @param s the string input that might have duplicates
     *  **/
    boolean hasDuplicates(String s) {
        for (int i = 0; i < s.length(); i += 1) {
            for (int j = i + 1; j < s.length(); j += 1) {
                if (s.charAt(i) == s.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
    }


    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw new EnigmaException("Index out of bounds");
        }
        return _chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw new EnigmaException("char not in alphabet");
        }
        return _chars.indexOf(ch);
    }

    /** Returns the _chars variable.
     *
     * @return _chars
     */
    String getAlphabet() {
        return _chars;
    }

    /** String that represents the characters in alphabet. **/
    private String _chars;

}
