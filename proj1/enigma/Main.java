package enigma;

import java.awt.font.NumericShaper;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Zachary Zhu
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        String message = "";
        while (_input.hasNextLine()) {
            setUp(m, _input.nextLine());
            while (_input.hasNext("[^*]+")) {
                message += m.convert(_input.next());
            }
        }
        printMessageLine(message);
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = Integer.parseInt(_config.next());
            int numPawls = Integer.parseInt(_config.next());
            ArrayList<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                Rotor r = readRotor();
                allRotors.add(r);
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        } catch (NumberFormatException excp) {
            throw error("num Rotors and numPawls should be int");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String typeNotches = _config.next();
            char type = typeNotches.charAt(0);
            String notches = typeNotches.substring(1);
            String rotorCycles = "";
            while (_config.hasNext("[(][^*]+[)]")) {
                rotorCycles += _config.next();
            }

            System.out.println("NAME: " + name);
            System.out.println("TYPE: " + type);
            System.out.println("NOTCHES: " + notches);
            System.out.println("CYCLES: " + rotorCycles);
            if (type == 'R') {
                return new Reflector(name, new Permutation(rotorCycles, _alphabet));
            } else if (type == 'N') {
                return new FixedRotor(name, new Permutation(rotorCycles, _alphabet));
            } else if (type == 'M') {
                if (notches.length() == 0) {
                    throw new EnigmaException("Empty notches for moving rotor");
                }
                return new MovingRotor(name, new Permutation(rotorCycles, _alphabet), notches);
            } else {
                throw new EnigmaException("Invalid rotor type");
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner configSettings = new Scanner(settings);
        if (!configSettings.next().equals("*")) {
            throw new EnigmaException("Doesn't start with asterisk");
        }
        ArrayList<String> names = new ArrayList<String>();
        while (configSettings.hasNext("[^(]")) {
            names.add(configSettings.next());
        }
        String setting = names.get(names.size() - 1);
        names.remove(names.size() - 1);
        String[] namesArray = (String[]) names.toArray();
        String plugboardString = "";
        while (configSettings.hasNext()) {
            plugboardString += configSettings.next();
        }
        M.insertRotors(namesArray);
        M.setRotors(setting);
        M.setPlugboard(new Permutation(plugboardString, _alphabet));

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        while (msg.length() >= 5) {
            _output.print(msg.substring(0, 5));
            msg = msg.substring(5);
        }
        if (msg.length() != 0) {
            _output.print(msg);
        }
        _output.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
