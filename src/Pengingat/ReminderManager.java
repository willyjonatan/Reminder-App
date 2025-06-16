package Pengingat;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ReminderManager {
    // [Private Instance Variable] Menyimpan daftar objek Reminder
    private ArrayList<Reminder> daftarReminder;

    // [Private Instance Variable] Menyimpan tracker notifikasi untuk Reminder
    private ArrayList<ReminderNotificationTracker> notificationTrackers;

    // [Constructor] Konstruktor default ReminderManager
    public ReminderManager() {
        daftarReminder = new ArrayList<>();
        notificationTrackers = new ArrayList<>();
    }

    // [Public Method] Menambahkan reminder dengan pengecekan duplikat (Encapsulation diterapkan)
    public boolean tambahReminder(Reminder reminder) {
        for (Reminder r : daftarReminder) {
            if (r.getTanggal().equals(reminder.getTanggal()) &&
                r.getWaktu().equals(reminder.getWaktu())) {
                return false;
            }
        }
        daftarReminder.add(reminder);
        return true;
    }

    // [Public Method] Menghapus reminder dan trackernya (Encapsulation diterapkan)
    public void hapusReminder(Reminder reminder) {
        daftarReminder.remove(reminder);
        
        // [Local Variable] untuk iterasi dan penghapusan tracker
        Iterator<ReminderNotificationTracker> iterator = notificationTrackers.iterator();
        while(iterator.hasNext()) {
            ReminderNotificationTracker tracker = iterator.next();
            if (tracker.reminder.equals(reminder)) {
                iterator.remove();
            }
        }
    }

    // [Public Getter Method] Mengembalikan daftar reminder
    public ArrayList<Reminder> getDaftarReminder() {
        return daftarReminder;
    }

    // [Public Method] Mengecek dan mengirimkan notifikasi untuk reminder yang waktunya hampir tiba
    public void checkReminders() {
        // [Local Variable] Menyimpan waktu saat ini
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Iterator<Reminder> reminderIterator = daftarReminder.iterator();

        while(reminderIterator.hasNext()) {
            Reminder reminder = reminderIterator.next();

            try {
                String reminderDateTimeStr = reminder.getTanggal() + " " + reminder.getWaktu();
                Date reminderDateTime = sdf.parse(reminderDateTimeStr);

                long diffMillis = now.getTime() - reminderDateTime.getTime();

                if (diffMillis >= 0 && diffMillis <= 180_000) {
                    ReminderNotificationTracker tracker = getTrackerForReminder(reminder);

                    if (tracker == null) {
                        // [Polymorphism] showNotification menerima Reminder sebagai parameter
                        tracker = new ReminderNotificationTracker(reminder);
                        notificationTrackers.add(tracker);
                        showNotification(reminder); // [Abstraction: disembunyikan cara notif munculnya]
                        tracker.incrementSent();
                    } else {
                        long millisSinceLastSent = now.getTime() - tracker.getLastSent().getTime();
                        if (tracker.getSentCount() < 3 && millisSinceLastSent >= 90_000) {
                            showNotification(reminder);
                            tracker.incrementSent();
                        }
                    }
                } else if(diffMillis > 180_000) {
                    removeTrackerForReminder(reminder);
                }
            } catch (ParseException e) {
                e.printStackTrace(); // [Abstraction: proses parsing dan error handling disembunyikan]
            }
        }
    }

    // [Private Method] Mendapatkan tracker berdasarkan reminder
    private ReminderNotificationTracker getTrackerForReminder(Reminder reminder) {
        for (ReminderNotificationTracker tracker : notificationTrackers) {
            if (tracker.reminder.equals(reminder)) {
                return tracker;
            }
        }
        return null;
    }

    // [Private Method] Menghapus tracker untuk reminder
    private void removeTrackerForReminder(Reminder reminder) {
        notificationTrackers.removeIf(tracker -> tracker.reminder.equals(reminder));
    }

    // [Private Method] Menampilkan notifikasi reminder (Abstraction)
    private void showNotification(Reminder reminder) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null,
                    "Pengingat : " + reminder.getNama() + "\n" +
                    "Deskripsi : " + reminder.getDeskripsi() + "\n" +
                    "Waktu : " + reminder.getWaktu(),
                    "Notifikasi Pengingat",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // [Private Static Nested Class] Digunakan untuk melacak pengiriman notifikasi
    private static class ReminderNotificationTracker {
        // [Private Final Instance Variable]
        private final Reminder reminder;

        // [Private Instance Variable]
        private int sentCount;

        // [Private Instance Variable]
        private Date lastSent;

        // [Constructor]
        public ReminderNotificationTracker(Reminder reminder) {
            this.reminder = reminder;
            this.sentCount = 0;
            this.lastSent = new Date(0);
        }

        // [Getter Method]
        public int getSentCount() {
            return sentCount;
        }

        // [Mutator Method]
        public void incrementSent() {
            sentCount++;
            lastSent = new Date(); // update waktu terakhir dikirim
        }

        // [Getter Method]
        public Date getLastSent() {
            return lastSent;
        }
    }

    // [Public Static Nested Class] Contoh method utilitas
    public static class ReminderStats {
        // [Public Static Method]
        public static int hitungTotal(ReminderManager manager) {
            return manager.daftarReminder.size();
        }
    }
}
