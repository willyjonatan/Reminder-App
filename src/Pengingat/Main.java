package Pengingat;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        ReminderManager reminderManager = new ReminderManager();

        // Langsung jalankan GUI
        SwingUtilities.invokeLater(() -> {
            new ReminderGUI(reminderManager).setVisible(true);
        });
    }
}
