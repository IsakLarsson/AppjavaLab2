public class Main {

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        try {
            Controller controller = new Controller();
        } catch (InterruptedException e) {
            System.out.println("Something went wrong with running the program");
        }
    }

}
