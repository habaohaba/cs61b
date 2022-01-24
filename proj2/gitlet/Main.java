package gitlet;

import java.util.Objects;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author LinZhuo
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> {
                Repository.setupRepository();
                Commit commitInit = new Commit();
                Repository.saveCommit(commitInit);
            }
            case "add" -> {
                Repository.initialized();
                Repository.add(args[1]);
            }
            case "commit" -> {
                Repository.initialized();
                if (args[1].length() == 0) {
                    System.out.println("Please enter a commit message.");
                } else {
                    Commit commitNew = new Commit(args[1]);
                    Repository.saveCommit(commitNew);
                }
            }
            case "rm" -> {
                Repository.initialized();
                Repository.rm(args[1]);
            }
            case "log" -> {
                Repository.initialized();
                Repository.log();
            }
            case "checkout" -> {
                Repository.initialized();
                if (args.length == 3) {
                    if (!Objects.equals(args[1], "--")) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    Repository.checkout(args[2]);
                } else if (args.length == 4) {
                    if (!Objects.equals(args[2], "--")) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    Repository.checkout(args[1], args[3]);
                } else if (args.length == 2) {
                    Repository.checkoutB(args[1]);
                }
            }
            case "global-log" -> {
                Repository.initialized();
                Repository.globalLog();
            }
            case "find" -> {
                Repository.initialized();
                Repository.find(args[1]);
            }
            case "status" -> {
                Repository.initialized();
                Repository.status();
            }
            case "branch" -> {
                Repository.initialized();
                Repository.branch(args[1]);
            }
            case "rm-branch" -> {
                Repository.initialized();
                Repository.rmBranch(args[1]);
            }
            case "reset" -> {
                Repository.initialized();
                Repository.reset(args[1]);
            }
            case "merge" -> {
                Repository.initialized();
                Repository.merge(args[1]);
            }
            default -> System.out.println("No command with that name exists.");
        }
        System.exit(0);
    }
}
