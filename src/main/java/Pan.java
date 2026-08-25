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
        int count = 0;
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            if (input.equals("list")) {
                for (int i = 0; i < count; i++) {
                    System.out.println(" " + (i + 1) + ". " + list[i]);
                }
            } else {
                list[count] = input;
                count++;
                System.out.println(" added: " + input);
            }
            System.out.println(LINE);
        }

        System.out.println("Byeee Byeee! PanPan will stay cute for you in the meantime! Mwah mwah~ (˘▾˘~)");
        System.out.println(LINE);
        scanner.close();
    }
}
