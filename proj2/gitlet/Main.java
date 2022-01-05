package gitlet;


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
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> {
                Repository.setupRepository();
                Commit commitInit = new Commit();
                Repository.saveCommit(commitInit);
            }
            case "add" -> Repository.add(args[1]);
            case "commit" -> {
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                } else {
                    String message = null;
                    if (args.length > 2) {
                        for (int i = 1; i < args.length; i++) {
                            message = message + " " + args[i];
                        }
                    } else {
                        message = args[1];
                    }
                    Commit commitNew = new Commit(message);
                    Repository.saveCommit(commitNew);
                }
            }
            case "rm" -> Repository.rm(args[1]);
            case "log" -> Repository.log();
            case "checkout" -> {
                if (args.length == 3) {
                    Repository.checkout(args[2]);
                } else if (args.length == 4) {
                    Repository.checkout(args[1], args[3]);
                } else if (args.length == 2) {
                    Repository.checkoutB(args[1]);
                }
            }
            case "global-log" -> Repository.globalLog();
            case "find" -> {
                if (args.length == 2) {
                    Repository.find(args[1]);
                } else {
                    String message = null;
                    if (args.length > 2) {
                        for (int i = 1; i < args.length; i++) {
                            if (message == null) {
                                message = args[i];
                            } else {
                                message = message + " " + args[i];
                            }
                        }
                    }
                    Repository.find(message);
                }
            }
            case "status" -> Repository.status();
            case "branch" -> Repository.branch(args[1]);
            case "rm-branch" -> Repository.rmBranch(args[1]);
            case "reset" -> Repository.reset(args[1]);
            case "merge" -> Repository.merge(args[1]);
            default -> System.out.println("No command with that name exists.");
        }
        System.exit(0);
    }
}
