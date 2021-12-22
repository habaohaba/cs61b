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
            case "init":
                // TODO: handle the `init` command
                Repository.setupRepository();
                Commit commitInit = new Commit();
                Repository.saveCommit(commitInit);
                break;
            case "add":
                Repository.add(args[1]);
                break;
            case "commit":
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
                break;
            case "rm":
                Repository.rm(args[1]);
                break;
            case "log":
                Repository.log();
                break;
            case "checkout":
                if (args.length == 3) {
                    Repository.checkout(args[2]);
                } else if (args.length == 4) {
                    Repository.checkout(args[1], args[3]);
                } else if (args.length == 2) {
                    Repository.checkoutB(args[1]);
                }
                break;
            default:
                System.out.println("No command with that name exists.");
        }
        System.exit(0);
    }
}
