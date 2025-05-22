package Term2;

import javax.swing.SwingUtilities;

public class Main {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MotorPHApp app = new MotorPHApp();
            app.setVisible(true);
        });
    }
}