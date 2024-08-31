package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     */
    public static void main(String[] args){
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        if (!args[0].equals("init")) {
            Repository.initializeCheck();
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length < 2) {
                    System.out.println("Please enter a commit message.");
                    break;
                }
                Repository.Commit(args[1]);
                break;
            case "rm":
                Repository.rm(args[1]);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.global_Log();
                break;
            case "find":
                Repository.find(args[1]);
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                    break;
                }
                if (args.length == 3 & args[1].equals("--")) {
                    Repository.checkoutFile(args[2]);
                    break;
                }
                if (args.length == 4 & args[2].equals("--")) {
                    Repository.checkoutCommitFile(args[1], args[3]);
                    break;
                }
                System.out.println("Incorrect operands.");
                break;
            case "branch":
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                Repository.reset(args[1]);
                break;
            case "merge":
                Repository.merge(args[1]);
                break;
            case "add-remote":
                Repository1.addRemote(args[1], args[2]);
                break;
            case "rm-remote":
                Repository1.rmRemote(args[1]);
                break;
            case "push":
                Repository1.push(args[1], args[2]);
                break;
            case "fetch":
                Repository1.fetch(args[1], args[2]);
                break;
            case "pull":
                Repository1.pull(args[1], args[2]);
                break;
            default:
                System.out.println("No command with that name exists.");
        }
    }
}
