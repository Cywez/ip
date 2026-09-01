import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point and command loop for the PanPan chatbot. Reads user commands
 * from standard input, maintains the task list, and persists it to disk via
 * {@link Storage} whenever the list changes.
 *
 * <p>Understanding the text of each command - the command word, its
 * arguments, and any dates inside them - is delegated to {@link Parser}.
 */
public class Pan {

    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        String banner = " ____      _     _   _ \n"
                + "|  _ \\    / \\   | \\ | |\n"
                + "| |_) |  / _ \\  |  \\| |\n"
                + "|  __/  / ___ \\ | |\\  |\n"
                + "|_|    /_/   \\_\\|_| \\_|\n";
        System.out.println(LINE);
        System.out.println(banner);
        System.out.println("Heyyy hihi~ (๑>ᴗ<๑) It's meeeee, PanPan!!");
        System.out.println("PanPan is SUPER happy you're here today, teehee~");
        System.out.println("What can PanPan do for you todayy??");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);
        // Load any previously saved tasks so the list survives between runs.
        Storage storage = new Storage();
        ArrayList<Task> tasks = storage.load();
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }

            // Split the raw line into "what to do" and "the rest".
            String command = Parser.commandWord(input);
            String arguments = Parser.arguments(input);

            try {
                switch (command) {
                case "list":
                    System.out.println(" Ooh ooh, here's what PanPan dug up for you~ "
                            + "PanPan's list-finding skills are Pan-tastic, teehee!!:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    break;

                case "mark": {
                    int index = parseTaskNumber(arguments, tasks.size(), "mark");
                    tasks.get(index).markAsDone();
                    storage.save(tasks);
                    System.out.println(" Yayyy!! PanPan marked this task as done, Pan-tastic job!!");
                    System.out.println("   " + tasks.get(index));
                    break;
                }

                case "unmark": {
                    int index = parseTaskNumber(arguments, tasks.size(), "unmark");
                    tasks.get(index).markAsNotDone();
                    storage.save(tasks);
                    System.out.println(" Awww not done yet? PanPan unmarked this task already... "
                            + "PanPan thinks you can do better!:");
                    System.out.println("   " + tasks.get(index));
                    break;
                }

                case "todo":
                    if (arguments.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Is there supposed to be something after todo?");
                    }
                    tasks.add(new Todo(arguments));
                    storage.save(tasks);
                    System.out.println(" PanPan added this todo to your list! " + tasks.get(tasks.size() - 1));
                    System.out.println(" PanPan will watch and make sure you do it!");
                    break;

                case "deadline":
                    tasks.add(Parser.parseDeadline(arguments));
                    storage.save(tasks);
                    System.out.println(" PanPan added this deadline to your list! " + tasks.get(tasks.size() - 1));
                    System.out.println(" PanPan will watch and make sure you do it!");
                    break;

                case "event":
                    tasks.add(Parser.parseEvent(arguments));
                    storage.save(tasks);
                    System.out.println(" PanPan added this event to your list! " + tasks.get(tasks.size() - 1));
                    System.out.println(" PanPan will watch and make sure you do it!");
                    break;

                case "delete": {
                    if (arguments.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Which task do you wanna delete?");
                    }
                    int index = parseTaskNumber(arguments, tasks.size(), "delete");
                    Task removed = tasks.get(index);
                    tasks.remove(index);
                    storage.save(tasks);
                    System.out.println(" Okayyy, PanPan waved byebye to this task and "
                            + "removed it from the list~ (｡•̀ᴗ-)✧");
                    System.out.println("   " + removed);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list!");
                    break;
                }

                default:
                    throw new PanException(" SORRYYY! PanPan don't know what that means. (╥﹏╥)");
                }
            } catch (PanException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(LINE);
        }

        System.out.println(" Byeee Byeee! PanPan will stay cute for you in the meantime! Mwah mwah~ (˘▾˘~)");
        System.out.println(LINE);
        scanner.close();
    }

    /**
     * Converts the user's 1-based task number into a validated 0-based list
     * index. Shared by {@code mark}, {@code unmark} and {@code delete}.
     *
     * @param arguments text after the command word, expected to be a number.
     * @param size      current number of tasks, for the range check.
     * @param command   the command word, so the error message can echo what
     *                  the user should have typed (e.g. {@code "mark 2"}).
     * @throws PanException if the text is not a number or is out of range.
     */
    private static int parseTaskNumber(String arguments, int size, String command) throws PanException {
        int index;
        try {
            index = Integer.parseInt(arguments.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new PanException(" Ooh wait wait~ PanPan needs a real task number, "
                    + "like \"" + command + " 2\", okay?? PanPan believes in youuu!! (๑˃́ꇴ˂̀๑)");
        }
        if (index < 0 || index >= size) {
            throw new PanException(" Ehh?? PanPan looked everywhere but that task number "
                    + "doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
        }
        return index;
    }
}
