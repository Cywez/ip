package pan;

import java.util.Scanner;

/**
 * Handles all interaction with the user: reading typed commands from standard
 * input and printing PanPan's responses to standard output.
 *
 * <p>Keeping every {@code System.out}/{@link Scanner} call in one class means the
 * rest of the program never talks to the console directly. If the output style
 * changes (or the chatbot grows a GUI later), only this class has to change.
 */
public class Ui {

    /** Divider printed between responses so the transcript stays readable. */
    private static final String LINE =
            "____________________________________________________________";

    /** The single shared input source for the whole session. */
    private final Scanner scanner = new Scanner(System.in);

    /** Reads the next command line the user types. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Prints the horizontal divider. */
    public void showLine() {
        System.out.println(LINE);
    }

    /** Prints the ASCII banner and greeting shown once at startup. */
    public void showWelcome() {
        String banner = " ____      _     _   _ \n"
                + "|  _ \\    / \\   | \\ | |\n"
                + "| |_) |  / _ \\  |  \\| |\n"
                + "|  __/  / ___ \\ | |\\  |\n"
                + "|_|    /_/   \\_\\|_| \\_|\n";
        showLine();
        System.out.println(banner);
        System.out.println("Heyyy hihi~ (๑>ᴗ<๑) It's meeeee, PanPan!!");
        System.out.println("PanPan is SUPER happy you're here today, teehee~");
        System.out.println("What can PanPan do for you todayy??");
        showLine();
    }

    /** Prints the farewell shown when the user types {@code bye}. */
    public void showGoodbye() {
        System.out.println(" Byeee Byeee! PanPan will stay cute for you in the meantime! Mwah mwah~ (˘▾˘~)");
    }

    /**
     * Prints an error message. The text is expected to be an already
     * PanPan-voiced message taken from a {@link PanException}.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Prints the warning shown when the save file cannot be read at startup. */
    public void showLoadingError() {
        System.out.println(" PanPan couldn't read the save file... starting fresh! (｡•́︿•̀｡)");
    }

    /** Prints the whole task list, numbered from 1. */
    public void showList(TaskList tasks) {
        System.out.println(" Ooh ooh, here's what PanPan dug up for you~ "
                + "PanPan's list-finding skills are Pan-tastic, teehee!!:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Prints the tasks that matched a {@code find} command, numbered from 1,
     * or a friendly note when nothing matched.
     *
     * @param matches the sub-list of tasks whose description contained the keyword.
     */
    public void showFound(TaskList matches) {
        if (matches.isEmpty()) {
            System.out.println(" Awww, PanPan looked hard but found no matching tasks~ (｡•́︿•̀｡)");
            return;
        }
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matches.get(i));
        }
    }

    /** Confirms a newly added task. Used for {@code todo}, {@code deadline} and {@code event}. */
    public void showAdded(Task task) {
        System.out.println(" PanPan added this to your list! " + task);
        System.out.println(" PanPan will watch and make sure you do it!");
    }

    /** Confirms a task was marked done. */
    public void showMarked(Task task) {
        System.out.println(" Yayyy!! PanPan marked this task as done, Pan-tastic job!!");
        System.out.println("   " + task);
    }

    /** Confirms a task was marked not done. */
    public void showUnmarked(Task task) {
        System.out.println(" Awww not done yet? PanPan unmarked this task already... "
                + "PanPan thinks you can do better!:");
        System.out.println("   " + task);
    }

    /**
     * Confirms a task was deleted.
     *
     * @param removed   the task that was removed.
     * @param remaining how many tasks are left afterwards.
     */
    public void showDeleted(Task removed, int remaining) {
        System.out.println(" Okayyy, PanPan waved byebye to this task and "
                + "removed it from the list~ (｡•̀ᴗ-)✧");
        System.out.println("   " + removed);
        System.out.println(" Now you have " + remaining + " tasks in the list!");
    }

    /** Closes the input source at the end of the session. */
    public void close() {
        scanner.close();
    }
}
