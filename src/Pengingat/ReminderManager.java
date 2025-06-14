package Pengingat;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ReminderManager {
    private ArrayList<Reminder> daftarReminder;
    private ArrayList<ReminderNotificationTracker> notificationTrackers;

    public ReminderManager() {
        daftarReminder = new ArrayList<>();
        notificationTrackers = new ArrayList<>();
    }

    public void tambahReminder(Reminder reminder) {
        daftarReminder.add(reminder);
    }

    public void hapusReminder(Reminder reminder) {
        daftarReminder.remove(reminder);
        // Juga hapus tracker terkait jika ada
        Iterator<ReminderNotificationTracker> iterator = notificationTrackers.iterator();
        while(iterator.hasNext()) {
            ReminderNotificationTracker tracker = iterator.next();
            if (tracker.reminder.equals(reminder)) {
                iterator.remove();
            }
        }
    }

    public ArrayList<Reminder> getDaftarReminder() {
        return daftarReminder;
    }

    // Method untuk cek reminder dan tampilkan notifikasi sesuai ketentuan:
    // Notifikasi muncul saat waktu pas, ulang 2 kali dalam 3 menit ke depan (misal: tiap 90 detik)
    public void checkReminders() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Iterator<Reminder> reminderIterator = daftarReminder.iterator();

        while(reminderIterator.hasNext()) {
            Reminder reminder = reminderIterator.next();

            try {
                String reminderDateTimeStr = reminder.getTanggal() + " " + reminder.getWaktu();
                Date reminderDateTime = sdf.parse(reminderDateTimeStr);

                long diffMillis = now.getTime() - reminderDateTime.getTime();

                // Cek jika waktunya sudah lewat atau pas dan dalam window 3 menit (180.000 ms)
                if (diffMillis >= 0 && diffMillis <= 180_000) {
                    // Cari tracker jika sudah ada
                    ReminderNotificationTracker tracker = getTrackerForReminder(reminder);

                    if (tracker == null) {
                        // Belum pernah notif, buat tracker baru dan tampilkan notif
                        tracker = new ReminderNotificationTracker(reminder);
                        notificationTrackers.add(tracker);
                        showNotification(reminder);
                        tracker.incrementSent();
                    } else {
                        // Tracker ada, cek apakah sudah kirim <2 kali dan delay 90 detik sejak terakhir notif
                        long millisSinceLastSent = now.getTime() - tracker.getLastSent().getTime();
                        if (tracker.getSentCount() < 3 && millisSinceLastSent >= 90_000) {
                            showNotification(reminder);
                            tracker.incrementSent();
                        }
                    }
                } else if(diffMillis > 180_000) {
                    // Jika sudah melewati 3 menit window, hapus tracker untuk pengingat ini
                    removeTrackerForReminder(reminder);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private ReminderNotificationTracker getTrackerForReminder(Reminder reminder) {
        for (ReminderNotificationTracker tracker : notificationTrackers) {
            if (tracker.reminder.equals(reminder)) {
                return tracker;
            }
        }
        return null;
    }

    private void removeTrackerForReminder(Reminder reminder) {
        notificationTrackers.removeIf(tracker -> tracker.reminder.equals(reminder));
    }

    private void showNotification(Reminder reminder) {
        // Tampilkan JOptionPane notifikasi (ini dipanggil di EDT dari thread check)
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null,
                    "Pengingat : " + reminder.getNama() + "\n" +
                    "Deskripsi : " + reminder.getDeskripsi() + "\n" +
                    "Waktu : " + reminder.getWaktu(),
                    "Notifikasi Pengingat",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // Class helper untuk tracking notifikasi per reminder
    private static class ReminderNotificationTracker {
        private final Reminder reminder;
        private int sentCount;
        private Date lastSent;

        public ReminderNotificationTracker(Reminder reminder) {
            this.reminder = reminder;
            this.sentCount = 0;
            this.lastSent = new Date(0);
        }

        public int getSentCount() {
            return sentCount;
        }

        public void incrementSent() {
            sentCount++;
            lastSent = new Date();
        }

        public Date getLastSent() {
            return lastSent;
        }
    }

    public static class ReminderStats {
        public static int hitungTotal(ReminderManager manager) {
            return manager.daftarReminder.size();
        }
    }
}

