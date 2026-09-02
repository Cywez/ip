import pan.Pan;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * A dependency-free test harness for Pan.
 *
 * Feeds a scripted sequence of commands into Pan's stdin (as if a user
 * typed them) and captures its stdout, so the whole todo/deadline/event
 * flow can be checked in one run instead of typing commands by hand.
 */
public class TestPan {

    public static void main(String[] args) throws Exception {
        String script = String.join("\n",
                "todo borrow book",
                "deadline return book /by 2019-12-01 1800",
                "event project meeting /from 2019-12-01 1400 /to 2019-12-01 1600",
                "deadline broken /by notadate",
                "list",
                "mark 1",
                "unmark 1",
                "mark 99",
                "bye"
        ) + "\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(script.getBytes()));
        System.setOut(new PrintStream(captured));

        Pan.main(new String[0]);

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = captured.toString();
        System.out.println(output);

        System.out.println("---- checks ----");
        check(output, "[T][ ] borrow book", "todo added correctly");
        check(output, "[D][ ] return book (by: Dec 01 2019, 6:00PM)", "deadline date parsed and reformatted");
        check(output, "[E][ ] project meeting (from: Dec 01 2019, 2:00PM to: Dec 01 2019, 4:00PM)",
                "event dates parsed and reformatted");
        check(output, "read that date", "unparseable date was rejected");
        check(output, "[X] borrow book", "mark 1 set task as done");
        check(output, "doesn't exist", "mark 99 (out of range) was rejected");
    }

    private static void check(String output, String expectedSnippet, String label) {
        if (output.contains(expectedSnippet)) {
            System.out.println("PASS: " + label);
        } else {
            System.out.println("FAIL: " + label + " (expected to find: \"" + expectedSnippet + "\")");
        }
    }
}
