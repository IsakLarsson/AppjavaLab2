import javax.swing.JOptionPane;

public class Main {

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        try {
            Controller controller = new Controller();
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, 
                "Something went wrong running the program: "
                        + e.getMessage());
        }
    }

}
