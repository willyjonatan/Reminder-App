package Pengingat;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ReminderManager reminderManager = new ReminderManager();

        System.out.println("Ingin menambahkan reminder? (ya/tidak):");
        String jawab = input.nextLine().trim().toLowerCase();

        while (jawab.equals("ya")) {
            System.out.print("Masukkan nama kegiatan: ");
            String nama = input.nextLine().trim();

            System.out.print("Masukkan waktu (HH:mm): ");
            String waktu = input.nextLine().trim();

            System.out.print("Masukkan tanggal (YYYY-MM-DD): ");
            String tanggal = input.nextLine().trim();

            System.out.print("Masukkan deskripsi kegiatan: ");
            String deskripsi = input.nextLine().trim();

            if (nama.isEmpty() || waktu.isEmpty() || tanggal.isEmpty() || deskripsi.isEmpty()) {
                System.out.println("⚠️  Semua field wajib diisi. Ulangi input.");
                continue;
            }

            Reminder reminder = new Reminder(nama, waktu, deskripsi, tanggal);
            reminderManager.tambahReminder(reminder);

            System.out.println("📌 Reminder berhasil ditambahkan.\n");

            System.out.print("Tambah lagi? (ya/tidak): ");
            jawab = input.nextLine().trim().toLowerCase();
        }

        input.close();

        // Jalankan GUI
        SwingUtilities.invokeLater(() -> {
            new ReminderGUI(reminderManager).setVisible(true);
        });
    }
}
