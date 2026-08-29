/**
 * Represents a deadline: a task that must be done by a given date or time.
 */
public class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }

    /**
     * Returns the save-file line for a deadline, e.g.
     * {@code "D | 0 | return book | June 6th"}.
     */
    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + by;
    }
}
