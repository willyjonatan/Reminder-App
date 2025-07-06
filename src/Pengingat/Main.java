package Pengingat;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        ReminderManager reminderManager = new ReminderManager();

        reminderManager.tambahReminder(
                new MeetingReminder("Meeting Proyek", "10:00", "Diskusi modul frontend", "2025-06-14", "Zoom")
                
        );

        SwingUtilities.invokeLater(() -> {
            ReminderGUI reminderGUI = new ReminderGUI(reminderManager);
            reminderGUI.setVisible(true);

            new Thread(() -> {
                while (true) {
                    reminderManager.checkReminders();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });
    }
}

