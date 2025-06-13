// File: ReminderManager.java

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

    public void hapusReminder(Reminder reminder) {
        daftarReminder.remove(reminder);
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
