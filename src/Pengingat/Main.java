// File: Main.java
package Pengingat;

import javax.swing.SwingUtilities;

// Kelas Main sebagai titik masuk program (Main class)
public class Main {

    // Method utama program (main method), dengan access modifier 'public' (ACCESS MODIFIER: public)
    public static void main(String[] args) {

        // Inisialisasi objek ReminderManager (VARIABEL OBJEK)
        ReminderManager reminderManager = new ReminderManager();

        // Tambahkan contoh reminder turunan (MeetingReminder) → POLYMORPHISM: MeetingReminder adalah subclass dari Reminder
        reminderManager.tambahReminder(
                new MeetingReminder("Meeting Proyek", "10:00", "Diskusi modul frontend", "2025-06-14", "Zoom")
                // CONSTRUCTOR MeetingReminder dipanggil di sini
        );

        // Jalankan GUI di thread event-dispatch menggunakan SwingUtilities (INTERFACE Runnable via lambda)
        SwingUtilities.invokeLater(() -> {
            // Membuat dan menampilkan GUI utama (VARIABEL OBJEK)
            ReminderGUI reminderGUI = new ReminderGUI(reminderManager);  // CONSTRUCTOR ReminderGUI
            reminderGUI.setVisible(true); // Method dari JFrame (INHERITANCE dari JFrame)

            // Thread terpisah untuk mengecek pengingat setiap detik (ABSTRACTION dari Thread class)
            new Thread(() -> {
                while (true) {
                    // Pengecekan pengingat dilakukan terus-menerus (METHOD dari ReminderManager)
                    reminderManager.checkReminders();

                    try {
                        // Tunda 1 detik (METHOD dari Thread class)
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // Menangani interupsi (EXCEPTION HANDLING)
                    }
                }
            }).start(); // Menjalankan thread (THREADING)
        });
    }
}
