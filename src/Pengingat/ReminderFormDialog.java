package Pengingat;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReminderFormDialog extends JDialog {
    private JTextField titleField;
    private JTextField timeField;
    private JTextField dateField;
    private JTextField descField;
    private JLabel charCounter;
    private Reminder reminder;
    private ReminderManager reminderManager;

    public ReminderFormDialog(Frame owner, Reminder reminder, ReminderManager reminderManager) {
        super(owner, reminder == null ? "Tambah Kegiatan" : "Edit Kegiatan", true);
        this.reminder = reminder;
        this.reminderManager = reminderManager;

        setSize(400, 300);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(new Color(240, 240, 255));
        setLayout(new BorderLayout());

        titleField = new JTextField(reminder != null ? reminder.getNama() : "");
        timeField = new JTextField(reminder != null ? reminder.getWaktu() : "");
        dateField = new JTextField(reminder != null ? reminder.getTanggal() : "");
        descField = new JTextField(reminder != null ? reminder.getDeskripsi() : "");

        charCounter = new JLabel((descField.getText().length()) + "/120");
        charCounter.setForeground(Color.GRAY);
        charCounter.setFont(new Font("Arial", Font.ITALIC, 10));

        descField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateCount(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateCount(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateCount(); }

            private void updateCount() {
                int len = descField.getText().length();
                charCounter.setText(len + "/120");
                charCounter.setForeground(len > 120 ? Color.RED : Color.GRAY);
            }
        });

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(240, 240, 255));

        formPanel.add(new JLabel("Judul:"));
        formPanel.add(titleField);
        formPanel.add(Box.createVerticalStrut(5));

        formPanel.add(new JLabel("Jam (HH:MM):"));
        formPanel.add(timeField);
        formPanel.add(Box.createVerticalStrut(5));

        formPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        formPanel.add(dateField);
        formPanel.add(Box.createVerticalStrut(5));

        formPanel.add(new JLabel("Deskripsi:"));
        formPanel.add(descField);
        formPanel.add(charCounter);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Batal");

        okButton.addActionListener(e -> {
            if (validateInput()) {
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> setVisible(false));

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean validateInput() {
        String title = titleField.getText().trim();
        String time = timeField.getText().trim();
        String date = dateField.getText().trim();
        String desc = descField.getText().trim();

        if (title.isEmpty() || time.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Judul, jam, dan tanggal tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isValidTime(time)) {
            JOptionPane.showMessageDialog(this, "Format waktu tidak valid. Gunakan format HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Format tanggal tidak valid. Gunakan format yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (desc.length() > 120) {
            JOptionPane.showMessageDialog(this, "Deskripsi tidak boleh lebih dari 120 karakter.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Periksa duplikat jadwal berdasarkan nama, tanggal, dan waktu
        for (Reminder existing : reminderManager.getDaftarReminder()) {
            if (existing != reminder &&
                existing.getNama().equalsIgnoreCase(title) &&
                existing.getTanggal().equals(date) &&
                existing.getWaktu().equals(time)) {

                JOptionPane.showMessageDialog(this, "Jadwal dengan judul, tanggal, dan waktu yang sama sudah ada.", "Duplikat Jadwal", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        if (reminder == null) {
            reminder = new Reminder(title, time, desc, date);
        } else {
            reminder.setNama(title);
            reminder.setWaktu(time);
            reminder.setTanggal(date);
            reminder.setDeskripsi(desc);
        }

        return true;
    }

    private boolean isValidTime(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        try {
            sdf.parse(timeStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public Reminder getReminder() {
        return reminder;
    }
}
