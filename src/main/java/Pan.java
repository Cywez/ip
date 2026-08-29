import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point and command loop for the PanPan chatbot. Reads user commands
 * from standard input, maintains the task list, and persists it to disk via
 * {@link Storage} whenever the list changes.
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

            try {
                if (input.equals("list")) {
                    System.out.println(" Ooh ooh, here's what PanPan dug up for you~ "
                            + "PanPan's list-finding skills are Pan-tastic, teehee!!:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }

                } else if (input.startsWith("mark ")) {
                    int index;
                    try {
                        index = Integer.parseInt(input.substring("mark ".length()).trim()) - 1;
                    } catch (NumberFormatException e) {
                        throw new PanException(" Ooh wait wait~ PanPan needs a real task number, "
                                + "like \"mark 2\", okay?? PanPan believes in youuu!! (๑˃́ꇴ˂̀๑)");
                    }
                    if (index < 0 || index >= tasks.size()) {
                        throw new PanException(" Ehh?? PanPan looked everywhere but that task number "
                                + "doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
                    }
                    tasks.get(index).markAsDone();
                    storage.save(tasks);
                    System.out.println(" Yayyy!! PanPan marked this task as done, Pan-tastic job!!");
                    System.out.println("   " + tasks.get(index));

                } else if (input.startsWith("unmark ")) {
                    int index;
                    try {
                        index = Integer.parseInt(input.substring("unmark ".length()).trim()) - 1;
                    } catch (NumberFormatException e) {
                        throw new PanException(" Ooh wait wait~ PanPan needs a real task number, "
                                + "like \"unmark 2\", okay?? PanPan believes in youuu!! (๑˃́ꇴ˂̀๑)");
                    }
                    if (index < 0 || index >= tasks.size()) {
                        throw new PanException(" Ehh?? PanPan looked everywhere but that task number "
                                + "doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
                    }
                    tasks.get(index).markAsNotDone();
                    storage.save(tasks);
                    System.out.println(" Awww not done yet? PanPan unmarked this task already... "
                            + "PanPan thinks you can do better!:");
                    System.out.println("   " + tasks.get(index));

                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.length() > "todo".length()
                            ? input.substring("todo ".length()).trim()
                            : "";
                    if (description.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Is there supposed to be something after todo?");
                    }
                    tasks.add(new Todo(description));
                    storage.save(tasks);
                    System.out.println(" PanPan added this todo to your list! " + tasks.get(tasks.size() - 1));
                    System.out.println(" PanPan will watch and make sure you do it!");

                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    String rest = input.length() > "deadline".length() ? input.substring("deadline ".length()) : "";
                    String[] parts = rest.split("/by", 2);
                    String description = parts[0].trim();
                    if (description.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Did you want PanPan to write something to deadline?");
                    }
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new PanException(" Urmm.. A deadline needs a /by date, PanPan can't guess it for you~");
                    }
                    String by = parts[1].trim();
                    tasks.add(new Deadline(description, by));
                    storage.save(tasks);
                    System.out.println(" PanPan added this deadline to your list! " + tasks.get(tasks.size() - 1));
                    System.out.println(" PanPan will watch and make sure you do it!");

                } else if (input.equals("event") || input.startsWith("event ")) {
                    String rest = input.length() > "event".length() ? input.substring("event ".length()) : "";
                    String[] parts = rest.split("/from", 2);
                    String description = parts[0].trim();
                    if (description.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Did you want PanPan to write something to event?");
                    }
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new PanException(" OOPS!!! An event needs both /from and /to, teehee~");
                    }
                    String[] fromToParts = parts[1].trim().split("/to", 2);
                    if (fromToParts.length < 2 || fromToParts[0].trim().isEmpty() || fromToParts[1].trim().isEmpty()) {
                        throw new PanException(" OOPS!!! An event needs both /from and /to, teehee~");
                    }
                    String start = fromToParts[0].trim();
                    String end = fromToParts[1].trim();
                    tasks.add(new Event(description, start, end));
                    storage.save(tasks);
                    System.out.println(" PanPan added this event to your list! " + tasks.get(tasks.size() - 1));
                    System.out.println(" PanPan will watch and make sure you do it!");

                } else if (input.equals("delete") || input.startsWith("delete ")) {
                    String indexStr = input.length() > "delete".length() ? input.substring("delete ".length()).trim() : "";
                    if (indexStr.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Which task do you wanna delete?");
                    }
                    int index;
                    try {
                        index = Integer.parseInt(indexStr) - 1;
                    } catch (NumberFormatException e) {
                        throw new PanException(" Ooh wait wait~ PanPan needs a real task number, "
                                + "like \"delete 2\", okay?? PanPan believes in youuu!! (๑˃́ꇴ˂̀๑)");
                    }
                    if (index < 0 || index >= tasks.size()) {
                        throw new PanException(" Ehh?? PanPan looked everywhere but that task number "
                                + "doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
                    }
                    Task removed = tasks.get(index);
                    tasks.remove(index);
                    storage.save(tasks);
                    System.out.println(" Okayyy, PanPan waved byebye to this task and "
                            + "removed it from the list~ (｡•̀ᴗ-)✧");
                    System.out.println("   " + removed);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list!");

                } else {
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
}
