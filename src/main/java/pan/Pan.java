package pan;

/**
 * Entry point for the PanPan chatbot. Wires together the three collaborators -
 * the user interface ({@link Ui}), the on-disk task store ({@link Storage}) and
 * the in-memory task list ({@link TaskList}) - and runs the command loop.
 *
 * <p>Understanding the text of each command (the command word, its arguments and
 * any dates inside them) is delegated to {@link Parser}.
 */
public class Pan {

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Builds the chatbot and loads any previously saved tasks so the list
     * survives between runs.
     */
    public Pan() {
        ui = new Ui();
        storage = new Storage();
        tasks = new TaskList(storage.load());
    }

    /**
     * Runs the read-eval-print loop: read a line, act on it, repeat until the
     * user types {@code bye}. The task list is saved after every change.
     */
    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();

            // Split the raw line into "what to do" and "the rest".
            String command = Parser.commandWord(input);
            String arguments = Parser.arguments(input);

            try {
                switch (command) {
                case "bye":
                    isExit = true;
                    break;

                case "list":
                    ui.showList(tasks);
                    break;

                case "mark": {
                    int index = parseTaskNumber(arguments, tasks.size(), "mark");
                    tasks.get(index).markAsDone();
                    storage.save(tasks.asList());
                    ui.showMarked(tasks.get(index));
                    break;
                }

                case "unmark": {
                    int index = parseTaskNumber(arguments, tasks.size(), "unmark");
                    tasks.get(index).markAsNotDone();
                    storage.save(tasks.asList());
                    ui.showUnmarked(tasks.get(index));
                    break;
                }

                case "todo":
                    if (arguments.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Is there supposed to be something after todo?");
                    }
                    tasks.add(new Todo(arguments));
                    storage.save(tasks.asList());
                    ui.showAdded(tasks.get(tasks.size() - 1));
                    break;

                case "deadline":
                    tasks.add(Parser.parseDeadline(arguments));
                    storage.save(tasks.asList());
                    ui.showAdded(tasks.get(tasks.size() - 1));
                    break;

                case "event":
                    tasks.add(Parser.parseEvent(arguments));
                    storage.save(tasks.asList());
                    ui.showAdded(tasks.get(tasks.size() - 1));
                    break;

                case "delete": {
                    if (arguments.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Which task do you wanna delete?");
                    }
                    int index = parseTaskNumber(arguments, tasks.size(), "delete");
                    Task removed = tasks.remove(index);
                    storage.save(tasks.asList());
                    ui.showDeleted(removed, tasks.size());
                    break;
                }

                default:
                    throw new PanException(" SORRYYY! PanPan don't know what that means. (╥﹏╥)");
                }
            } catch (PanException e) {
                ui.showError(e.getMessage());
            }

            // The divider is printed after every response except the farewell.
            if (!isExit) {
                ui.showLine();
            }
        }

        ui.showGoodbye();
        ui.showLine();
        ui.close();
    }

    public static void main(String[] args) {
        new Pan().run();
    }

    /**
     * Converts the user's 1-based task number into a validated 0-based list
     * index. Shared by {@code mark}, {@code unmark} and {@code delete}.
     *
     * @param arguments text after the command word, expected to be a number.
     * @param size      current number of tasks, for the range check.
     * @param command   the command word, so the error message can echo what the
     *                  user should have typed (e.g. {@code "mark 2"}).
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
