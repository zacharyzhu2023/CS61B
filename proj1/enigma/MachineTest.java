package enigma;
import java.util.ArrayList;
import org.junit.Test;

public class MachineTest {

    @Test
    public void firstMachine() {
        Alphabet alpha  = new Alphabet();
        int numRotors = 5;
        int numPawls = 3;
        MovingRotor r1 = new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) "
                        + "(CMOY) (DFG) (IV) (JZ) (S)",
                        new Alphabet()), "Q");
        MovingRotor r3 = new MovingRotor("III",
                new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)",
                        new Alphabet()), "E");
        MovingRotor r4 = new MovingRotor("IV",
                new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)",
                        new Alphabet()), "V");
        FixedRotor beta = new FixedRotor("Beta",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)",
                        new Alphabet()));
        Reflector b = new Reflector("B",
                new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) "
                        + "(IJ) (LO) (MP) (RX) (SZ) (TV)",
                        new Alphabet()));
        ArrayList<Rotor> rotors = new ArrayList<Rotor>();
        rotors.add(r3);
        rotors.add(r1);
        rotors.add(r4);
        rotors.add(b);
        rotors.add(beta);

        Machine firstMachine = new Machine(alpha, 5, 3, rotors);

        String[] order = new String[]{"B", "Beta", "III", "IV", "I"};
        firstMachine.insertRotors(order);
        firstMachine.setRotors("AXLE");

        Permutation plugboard = new Permutation("(ZH)(YF)", new Alphabet());
        firstMachine.setPlugboard(plugboard);
        firstMachine.convert("Y");

    }
}
