/**
 * Represents an event: a task that runs from a start time to an end time.
 */
public class Event extends Task {
    private String start;
    private String end;

    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.start + " to: " + this.end + ")";
    }

    /**
     * Returns the save-file line for an event, e.g.
     * {@code "E | 0 | project meeting | Mon 2pm | 4pm"}. The start and end
     * are kept as separate fields so loading maps straight back to the
     * {@code Event(description, start, end)} constructor.
     */
    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + start + " | " + end;
    }
}
