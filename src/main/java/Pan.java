import java.util.Scanner;

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
        Task[] tasks = new Task[100];
        int count = 0;
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }

            try {
                if (input.equals("list")) {
                    System.out.println(" Ooh ooh, here's what PanPan dug up for you~ PanPan's list-finding skills are Pan-tastic, teehee!!:");
                    for (int i = 0; i < count; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }

                } else if (input.startsWith("mark ")) {
                    int index;
                    try {
                        index = Integer.parseInt(input.substring(5).trim()) - 1;
                    } catch (NumberFormatException e) {
                        throw new PanException(" Ooh wait wait~ PanPan needs a real task number, like \"mark 2\", okay?? PanPan believes in youuu!! (๑˃́ꇴ˂̀๑)");
                    }
                    if (index < 0 || index >= count) {
                        throw new PanException(" Ehh?? PanPan looked everywhere but that task number doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
                    }
                    tasks[index].markAsDone();
                    System.out.println(" Yayyy!! PanPan marked this task as done, Pan-tastic job!!");
                    System.out.println("   " + tasks[index]);

                } else if (input.startsWith("unmark ")) {
                    int index;
                    try {
                        index = Integer.parseInt(input.substring(7).trim()) - 1;
                    } catch (NumberFormatException e) {
                        throw new PanException(" Ooh wait wait~ PanPan needs a real task number, like \"unmark 2\", okay?? PanPan believes in youuu!! (๑˃́ꇴ˂̀๑)");
                    }
                    if (index < 0 || index >= count) {
                        throw new PanException(" Ehh?? PanPan looked everywhere but that task number doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
                    }
                    tasks[index].markAsNotDone();
                    System.out.println(" Awww not done yet? PanPan unmarked this task already... PanPan thinks you can do better!:");
                    System.out.println("   " + tasks[index]);

                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.length() > 4 ? input.substring(5).trim() : "";
                    if (description.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Is there supposed to be something after todo?");
                    }
                    tasks[count] = new Todo(description);
                    count++;
                    System.out.println(" PanPan added this todo to your list! " + tasks[count - 1]);
                    System.out.println(" PanPan will watch and make sure you do it!");

                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    String rest = input.length() > 8 ? input.substring(9) : "";
                    String[] parts = rest.split("/by", 2);
                    String description = parts[0].trim();
                    if (description.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Did you want PanPan to write something to deadline?");
                    }
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new PanException(" Urmm.. A deadline needs a /by date, PanPan can't guess it for you~");
                    }
                    String by = parts[1].trim();
                    tasks[count] = new Deadline(description, by);
                    count++;
                    System.out.println(" PanPan added this deadline to your list! " + tasks[count - 1]);
                    System.out.println(" PanPan will watch and make sure you do it!");

                } else if (input.equals("event") || input.startsWith("event ")) {
                    String rest = input.length() > 5 ? input.substring(6) : "";
                    String[] parts = rest.split("/from", 2);
                    String description = parts[0].trim();
                    if (description.isEmpty()) {
                        throw new PanException(" Ehhh? PanPan is confused... Did you want PanPan to write something to event?");
                    }
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new PanException(" OOPS!!! An event needs both /from and /to, teehee~");
                    }
                    String[] parts2 = parts[1].trim().split("/to", 2);
                    if (parts2.length < 2 || parts2[0].trim().isEmpty() || parts2[1].trim().isEmpty()) {
                        throw new PanException(" OOPS!!! An event needs both /from and /to, teehee~");
                    }
                    String start = parts2[0].trim();
                    String end = parts2[1].trim();
                    tasks[count] = new Event(description, start, end);
                    count++;
                    System.out.println(" PanPan added this event to your list! " + tasks[count - 1]);
                    System.out.println(" PanPan will watch and make sure you do it!");
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
