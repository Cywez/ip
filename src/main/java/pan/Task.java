package pan;

/**
 * Represents a single task with a description and a done/not-done status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /** Creates a task with the given description, initially not done. */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns the on-disk representation of this task's status and
     * description, e.g. {@code "1 | read book"}. Subclasses prepend their
     * type tag and append any extra fields. This is deliberately separate
     * from {@link #toString()}, which produces the pretty display format.
     */
    public String toFileString() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
