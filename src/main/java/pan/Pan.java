package pan;

/**
 * Entry point for the PanPan chatbot. Wires together the three collaborators -
 * the user interface ({@link Ui}), the on-disk task store ({@link Storage}) and
 * the in-memory task list ({@link TaskList}) - and turns one line of user input
 * into one reply.
 *
 * <p>Understanding the text of each command (the command word, its arguments and
 * any dates inside them) is delegated to {@link Parser}. The reply is produced by
 * {@link #getResponse(String)}, which is called both by the console loop in
 * {@link #run()} and by the JavaFX window in {@link Main}.
 */
public class Pan {

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /** Set to {@code true} once the user has asked to exit with {@code bye}. */
    private boolean isExit;

    /**
     * Builds the chatbot and loads any previously saved tasks so the list
     * survives between runs.
     */
    public Pan() {
        ui = new Ui();
        storage = new Storage();
        tasks = new TaskList(storage.load());
        isExit = false;
    }

    /**
     * Runs the console read-eval-print loop: read a line, print
     * {@link #getResponse(String)} for it, repeat until the user types
     * {@code bye}.
     */
    public void run() {
        ui.showWelcome();

        while (!isExit) {
            String input = ui.readCommand();
            System.out.println(getResponse(input));
            if (!isExit) {
                ui.showLine();
            }
        }

        ui.showLine();
        ui.close();
    }

    /**
     * Acts on one line of user input and returns PanPan's reply as text. The
     * task list is saved after every change.
     *
     * <p>This method never throws: a {@link PanException} from bad input is
     * caught and its (already PanPan-voiced) message is returned as the reply.
     *
     * @param input the raw line the user typed.
     * @return the reply to show the user.
     */
    public String getResponse(String input) {
        String command = Parser.commandWord(input);
        String arguments = Parser.arguments(input);

        try {
            switch (command) {
            case "bye":
                isExit = true;
                return ui.getGoodbye();

            case "list":
                return ui.formatList(tasks);

            case "find":
                if (arguments.isEmpty()) {
                    throw new PanException(" Ehhh? PanPan is confused... "
                            + "What word should PanPan look for?");
                }
                return ui.formatFound(tasks.find(arguments));

            case "mark": {
                int index = parseTaskNumber(arguments, tasks.size(), "mark");
                tasks.get(index).markAsDone();
                storage.save(tasks.asList());
                return ui.formatMarked(tasks.get(index));
            }

            case "unmark": {
                int index = parseTaskNumber(arguments, tasks.size(), "unmark");
                tasks.get(index).markAsNotDone();
                storage.save(tasks.asList());
                return ui.formatUnmarked(tasks.get(index));
            }

            case "todo":
                if (arguments.isEmpty()) {
                    throw new PanException(" Ehhh? PanPan is confused... "
                            + "Is there supposed to be something after todo?");
                }
                tasks.add(new Todo(arguments));
                storage.save(tasks.asList());
                return ui.formatAdded(tasks.get(tasks.size() - 1));

            case "deadline":
                tasks.add(Parser.parseDeadline(arguments));
                storage.save(tasks.asList());
                return ui.formatAdded(tasks.get(tasks.size() - 1));

            case "event":
                tasks.add(Parser.parseEvent(arguments));
                storage.save(tasks.asList());
                return ui.formatAdded(tasks.get(tasks.size() - 1));

            case "delete": {
                if (arguments.isEmpty()) {
                    throw new PanException(" Ehhh? PanPan is confused... Which task do you wanna delete?");
                }
                int index = parseTaskNumber(arguments, tasks.size(), "delete");
                Task removed = tasks.remove(index);
                storage.save(tasks.asList());
                return ui.formatDeleted(removed, tasks.size());
            }

            default:
                throw new PanException(" SORRYYY! PanPan don't know what that means. (╥﹏╥)");
            }
        } catch (PanException e) {
            return e.getMessage();
        }
    }

    /** Returns PanPan's opening greeting, for the GUI to show before any input. */
    public String getWelcome() {
        return ui.getWelcome();
    }

    /** Returns {@code true} once the user has typed {@code bye}. */
    public boolean isExit() {
        return isExit;
    }

    /** Launches the console version of the PanPan chatbot. */
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
                    + "like \"" + command + " 2\", okay?? PanPan believes in youuu!!");
        }
        if (index < 0 || index >= size) {
            throw new PanException(" Ehh?? PanPan looked everywhere but that task number "
                    + "doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
        }
        return index;
    }
}