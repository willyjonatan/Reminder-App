package Pengingat;

import java.util.ArrayList;

public class ReminderManager {
    private ArrayList<Reminder> daftarReminder;

    public ReminderManager() {
        daftarReminder = new ArrayList<>();
    }

    public void tambahReminder(Reminder reminder) {
        daftarReminder.add(reminder);
    }

    public void hapusSemuaReminder() {
        daftarReminder.clear();
    }

    public ArrayList<Reminder> getDaftarReminder() {
        return daftarReminder;
    }

    public static class ReminderStats {
        public static int hitungTotal(ReminderManager manager) {
            return manager.daftarReminder.size();
        }
    }
}
