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
                return handleBye();
            case "list":
                return ui.formatList(tasks);
            case "find":
                return handleFind(arguments);
            case "todo":
                return handleTodo(arguments);
            case "deadline":
                return addTask(Parser.parseDeadline(arguments));
            case "event":
                return addTask(Parser.parseEvent(arguments));
            case "mark":
                return handleMark(arguments);
            case "unmark":
                return handleUnmark(arguments);
            case "delete":
                return handleDelete(arguments);
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

    /** Records the user's wish to quit and returns the farewell. */
    private String handleBye() {
        isExit = true;
        return ui.getGoodbye();
    }

    /** Returns the tasks whose description contains the keyword the user gave. */
    private String handleFind(String arguments) throws PanException {
        requireArguments(arguments,
                " Ehhh? PanPan is confused... What word should PanPan look for?");
        return ui.formatFound(tasks.find(arguments));
    }

    /** Adds a todo whose description is the whole of the argument text. */
    private String handleTodo(String arguments) throws PanException {
        requireArguments(arguments,
                " Ehhh? PanPan is confused... Is there supposed to be something after todo?");
        return addTask(new Todo(arguments));
    }

    /** Marks the task the user numbered as done. */
    private String handleMark(String arguments) throws PanException {
        Task task = tasks.get(parseTaskNumber(arguments, tasks.size(), "mark"));
        task.markAsDone();
        saveTasks();
        return ui.formatMarked(task);
    }

    /** Marks the task the user numbered as not done. */
    private String handleUnmark(String arguments) throws PanException {
        Task task = tasks.get(parseTaskNumber(arguments, tasks.size(), "unmark"));
        task.markAsNotDone();
        saveTasks();
        return ui.formatUnmarked(task);
    }

    /** Removes the task the user numbered from the list. */
    private String handleDelete(String arguments) throws PanException {
        requireArguments(arguments,
                " Ehhh? PanPan is confused... Which task do you wanna delete?");
        Task removed = tasks.remove(parseTaskNumber(arguments, tasks.size(), "delete"));
        saveTasks();
        return ui.formatDeleted(removed, tasks.size());
    }

    /**
     * Adds a task, saves the updated list and returns the confirmation
     * message. Shared by the {@code todo}, {@code deadline} and {@code event}
     * commands, which differ only in how they build the task.
     *
     * @param task the newly built task to add.
     * @return the reply confirming the addition.
     */
    private String addTask(Task task) {
        tasks.add(task);
        saveTasks();
        return ui.formatAdded(task);
    }

    /** Writes the current task list to disk so it survives a restart. */
    private void saveTasks() {
        storage.save(tasks.asList());
    }

    /**
     * Rejects a command that needs arguments but was given none.
     *
     * @param arguments the text after the command word.
     * @param message   the PanPan-voiced complaint to show the user.
     * @throws PanException if {@code arguments} is empty.
     */
    private static void requireArguments(String arguments, String message) throws PanException {
        if (arguments.isEmpty()) {
            throw new PanException(message);
        }
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
        assert index >= 0 && index < size
                : "the range check above should guarantee a valid index at this point";
        return index;
    }
}