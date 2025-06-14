package Pengingat;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        ReminderManager reminderManager = new ReminderManager();

        // Langsung jalankan GUI
        SwingUtilities.invokeLater(() -> {
            ReminderGUI reminderGUI = new ReminderGUI(reminderManager);
            reminderGUI.setVisible(true);

            // Thread untuk memeriksa pengingat setiap detik
            new Thread(() -> {
                while (true) {
                    reminderManager.checkReminders();
                    try {
                        Thread.sleep(1000); // Tunggu 1 detik
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });
    }
}
