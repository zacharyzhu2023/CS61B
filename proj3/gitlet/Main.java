package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Zachary Zhu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        Commands commands = new Commands();
        if (args.length == 0) {
            System.out.println("Please enter a command");
            System.exit(0);
        } else if (args[0].equals("init")) {
            commands.init();
        } else {
            if (!Utils.join(".", ".gitlet").exists()) {
                System.out.println("Not in an initialized Gitlet directory.");
                System.exit(0);
            }
            if (args[0].equals("add")) {
                commands.add(args[1]);
            } else if (args[0].equals("rm")) {
                commands.remove(args[1]);
            } else if (args[0].equals("commit")) {
                commands.commit(args[1]);
            } else if (args[0].equals("log")) {
                commands.log();
            } else if (args[0].equals("global-log")) {
                commands.globalLog();
            } else if (args[0].equals("find")) {
                commands.find(args[1]);
            } else if (args[0].equals("status")) {
                commands.status();
            } else if (args[0].equals("checkout")) {
                if (args.length == 4) {
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    commands.checkoutIDFile(args[1], args[3]);
                } else if (args.length == 2) {
                    commands.checkoutBranch(args[1]);
                } else if (args.length == 3) {
                    commands.checkoutFile(args[2]);
                }
            } else if (args[0].equals("branch")) {
                commands.branch(args[1]);
            } else if (args[0].equals("rm-branch")) {
                commands.removeBranch(args[1]);
            } else if (args[0].equals("reset")) {
                commands.reset(args[1]);
            } else if (args[0].equals("merge")) {
                commands.merge(args[1]);
            } else {
                System.out.println("No command with that name exists.");
                System.exit(0);
            }
        }
    }

}
