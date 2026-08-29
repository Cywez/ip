import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles persisting the task list to disk and reading it back.
 *
 * <p>The data lives in a plain text file at the relative path
 * {@code ./data/pan.txt}. Using a relative, separator-neutral path means
 * the chatbot behaves the same regardless of which folder it is run from
 * or which operating system is used.
 */
public class Storage {

    /**
     * Location of the save file. The two-argument {@link File} constructor
     * joins {@code "data"} and {@code "pan.txt"} with the correct separator
     * for the current OS, so no {@code '/'} or {@code '\\'} is hard-coded.
     */
    private static final File FILE = new File("data", "pan.txt");

    /**
     * Loads the saved tasks from disk.
     *
     * @return the tasks stored in the file, or an empty list if the file
     *         does not exist yet (for example, the very first run on a new
     *         machine).
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!FILE.exists()) {
            return tasks;
        }
        try (Scanner scanner = new Scanner(FILE)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    tasks.add(parseTask(line));
                }
            }
        } catch (IOException e) {
            System.out.println(" PanPan couldn't read the save file... starting fresh! (｡•́︿•̀｡)");
        }
        return tasks;
    }

    /**
     * Writes the whole task list to disk, replacing any previous contents.
     * Creates the {@code ./data} folder first if it is missing.
     *
     * @param tasks the current task list to persist.
     */
    public void save(ArrayList<Task> tasks) {
        try {
            File parent = FILE.getParentFile();
            if (parent != null) {
                parent.mkdirs(); // no-op if the folder already exists
            }
            try (FileWriter writer = new FileWriter(FILE)) {
                for (Task task : tasks) {
                    writer.write(task.toFileString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.out.println(" PanPan couldn't save your tasks... sorryyy! (╥﹏╥)");
        }
    }

    /**
     * Rebuilds a single {@link Task} from one line of the save file.
     * The expected line format is
     * {@code TYPE | STATUS | DESCRIPTION [ | EXTRA FIELDS ]}.
     */
    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
        case "D":
            task = new Deadline(description, parts[3]);
            break;
        case "E":
            task = new Event(description, parts[3], parts[4]);
            break;
        default:
            task = new Todo(description);
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
