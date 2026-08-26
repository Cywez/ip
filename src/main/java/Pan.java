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
            if (input.equals("list")) {
                System.out.println(" Ooh ooh, here's what PanPan dug up for you~ PanPan's list-finding skills are Pan-tastic, teehee!!:");
                for (int i = 0; i < count; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5).trim()) - 1;
                    if (index < 0 || index >= count) {
                        System.out.println(" Ehh?? PanPan looked everywhere but that task number doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
                    } else {
                        tasks[index].markAsDone();
                        System.out.println(" Yayyy!! PanPan marked this task as done, Pan-tastic job!!");
                        System.out.println("   " + tasks[index]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Ooh wait wait~ PanPan needs a real task number, like \"mark 2\", okay?? PanPan believes in youuu!! (๑˃́ꇴ˂̀๑)");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int index = Integer.parseInt(input.substring(7).trim()) - 1;
                    if (index < 0 || index >= count) {
                        System.out.println(" Ehh?? PanPan looked everywhere but that task number doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
                    } else {
                        tasks[index].markAsNotDone();
                        System.out.println(" Awww not done yet? PanPan unmarked this task already... PanPan thinks you can do better!:");
                        System.out.println("   " + tasks[index]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Ooh wait wait~ PanPan needs a real task number, like \"unmark 2\", okay?? PanPan believes in youuu!! (๑˃́ꇴ˂̀๑)");
                }
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                tasks[count] = new Todo(description);
                count++;
                System.out.println(" PanPan added this todo to your list! " + tasks[count - 1]);
                System.out.println(" PanPan will watch and make sure you do it!");
            } else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split("/by", 2);
                String description = parts[0].trim();
                String by = parts[1].trim();
                tasks[count] = new Deadline(description, by);
                count++;
                System.out.println(" PanPan added this deadline to your list! " + tasks[count - 1]);
                System.out.println(" PanPan will watch and make sure you do it!");
            } else if (input.startsWith("event ")) {
                String[] parts = input.substring(6).split("/from", 2);
                String description = parts[0].trim();
                String[] parts2 = parts[1].trim().split("/to", 2);
                String start = parts2[0].trim();
                String end = parts2[1].trim();
                tasks[count] = new Event(description, start, end);
                count++;
                System.out.println(" PanPan added this event to your list! " + tasks[count - 1]);
                System.out.println(" PanPan will watch and make sure you do it!");
            } else {
                tasks[count] = new Task(input);
                count++;
                System.out.println(" PanPan added " + tasks[count - 1] + " to your list! PanPan gets bamboo treats? (´,,•ω•,,)");
            }
            System.out.println(LINE);
        }

        System.out.println(" Byeee Byeee! PanPan will stay cute for you in the meantime! Mwah mwah~ (˘▾˘~)");
        System.out.println(LINE);
        scanner.close();
    }
}
