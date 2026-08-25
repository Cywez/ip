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
        String[] list = new String[100];
        // Parallel array tracking done-state per task (no Task class, per project scope).
        boolean[] done = new boolean[100];
        int count = 0;
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            if (input.equals("list")) {
                System.out.println(" Ooh ooh, here's what PanPan dug up for you~ PanPan's list-finding skills are Pan-tastic, teehee!!:");
                for (int i = 0; i < count; i++) {
                    System.out.println(" " + (i + 1) + "." + (done[i] ? "[X]" : "[ ]") + " " + list[i]);
                }
            } else if (input.startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5).trim()) - 1;
                    if (index < 0 || index >= count) {
                        System.out.println(" Ehh?? PanPan looked everywhere but that task number doesn't exist~ (｡•́︿•̀｡) PanPan is confused!!");
                    } else {
                        done[index] = true;
                        System.out.println(" Yayyy!! PanPan marked this task as done, Pan-tastic job!!");
                        System.out.println("   [X] " + list[index]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Ooh wait wait~ PanPan needs a real task number, like \"mark 2\", okay?? PanPan believes in youuu!! (๑˃́ꇴ˂̀๑)");
                }
            } else {
                list[count] = input;
                count++;
                System.out.println(" PanPan added " + input + " to your list! PanPan gets bamboo treats? (´,,•ω•,,)");
            }
            System.out.println(LINE);
        }

        System.out.println("Byeee Byeee! PanPan will stay cute for you in the meantime! Mwah mwah~ (˘▾˘~)");
        System.out.println(LINE);
        scanner.close();
    }
}
