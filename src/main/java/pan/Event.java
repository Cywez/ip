package pan;

import java.time.LocalDateTime;

/**
 * Represents an event: a task that runs from a start date/time to an end
 * date/time.
 */
public class Event extends Task {

    /** When the event starts. */
    private LocalDateTime start;

    /** When the event ends. */
    private LocalDateTime end;

    /** Creates an event with the given description, running from {@code start} to {@code end}. */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + Parser.formatDateTime(start)
                + " to: " + Parser.formatDateTime(end) + ")";
    }

    /**
     * Returns the save-file line for an event, e.g.
     * {@code "E | 0 | project meeting | 2019-10-15T14:00 | 2019-10-15T16:00"}.
     * Start and end are stored as separate ISO-8601 fields so loading maps
     * straight back to the {@code Event(description, start, end)} constructor.
     */
    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + start + " | " + end;
    }
}
