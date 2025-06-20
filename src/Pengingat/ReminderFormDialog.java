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
    private JTextField extraField;
    private JComboBox<String> typeComboBox;
    private JLabel charCounter;
    private Reminder reminder;
    private ReminderManager reminderManager;

    public ReminderFormDialog(Frame owner, Reminder reminder, ReminderManager reminderManager) {
        super(owner, reminder == null ? "Tambah Kegiatan" : "Edit Kegiatan", true);
        this.reminder = reminder;
        this.reminderManager = reminderManager;

        setSize(400, 400);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(new Color(240, 240, 255));
        setLayout(new BorderLayout());

        titleField = new JTextField(reminder != null ? reminder.getNama() : "");
        titleField.setHorizontalAlignment(JTextField.LEFT);

        timeField = new JTextField(reminder != null ? reminder.getWaktu() : "");
        timeField.setHorizontalAlignment(JTextField.LEFT);

        dateField = new JTextField(reminder != null ? reminder.getTanggal() : "");
        dateField.setHorizontalAlignment(JTextField.LEFT);

        descField = new JTextField(reminder != null ? reminder.getDeskripsi() : "");
        descField.setHorizontalAlignment(JTextField.LEFT);

        extraField = new JTextField();
        extraField.setHorizontalAlignment(JTextField.LEFT);

        typeComboBox = new JComboBox<>(new String[]{"Reminder Biasa", "Meeting Reminder", "Deadline Reminder"});

        if (reminder instanceof MeetingReminder) {
            typeComboBox.setSelectedIndex(1);
            extraField.setText(((MeetingReminder) reminder).getPlatform());
        } else if (reminder instanceof DeadlineReminder) {
            typeComboBox.setSelectedIndex(2);
            extraField.setText(((DeadlineReminder) reminder).getPrioritas());
        }

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

// Komponen rata kiri
formPanel.add(makeLeftLabel("Jenis Kegiatan:"));
typeComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
formPanel.add(typeComboBox);
formPanel.add(Box.createVerticalStrut(5));

formPanel.add(makeLeftLabel("Judul:"));
titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
formPanel.add(titleField);
formPanel.add(Box.createVerticalStrut(5));

formPanel.add(makeLeftLabel("Waktu (HH:mm):"));
timeField.setAlignmentX(Component.LEFT_ALIGNMENT);
formPanel.add(timeField);
formPanel.add(Box.createVerticalStrut(5));

formPanel.add(makeLeftLabel("Tanggal (yyyy-MM-dd):"));
dateField.setAlignmentX(Component.LEFT_ALIGNMENT);
formPanel.add(dateField);
formPanel.add(Box.createVerticalStrut(5));

formPanel.add(makeLeftLabel("Deskripsi:"));
descField.setAlignmentX(Component.LEFT_ALIGNMENT);
formPanel.add(descField);
charCounter.setAlignmentX(Component.LEFT_ALIGNMENT);
formPanel.add(charCounter);
formPanel.add(Box.createVerticalStrut(5));

formPanel.add(makeLeftLabel("Platform / Prioritas:"));
extraField.setAlignmentX(Component.LEFT_ALIGNMENT);
formPanel.add(extraField);

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

    private JLabel makeLeftLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private boolean validateInput() {
        String title = titleField.getText().trim();
        String time = timeField.getText().trim();
        String date = dateField.getText().trim();
        String desc = descField.getText().trim();
        String extra = extraField.getText().trim();

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

        for (Reminder existing : reminderManager.getDaftarReminder()) {
            if (reminder != null && existing == reminder) continue;
            if (existing.getTanggal().equals(date) && existing.getWaktu().equals(time)) {
                JOptionPane.showMessageDialog(this, "Sudah ada kegiatan lain pada waktu yang sama.", "Jadwal Bentrok", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        int selectedType = typeComboBox.getSelectedIndex();
        if (reminder == null) {
            switch (selectedType) {
                case 1:
                    reminder = new MeetingReminder(title, time, desc, date, extra);
                    break;
                case 2:
                    reminder = new DeadlineReminder(title, time, desc, date, extra);
                    break;
                default:
                    reminder = new Reminder(title, time, desc, date) {
                        @Override
                        public String display() {
                            return "Judul: " + getNama() +
                                   "\nWaktu: " + getWaktu() +
                                   "\nTanggal: " + getTanggal() +
                                   "\nDeskripsi: " + getDeskripsi();
                        }
                    };
            }
        } else {
            reminder.setNama(title);
            reminder.setWaktu(time);
            reminder.setTanggal(date);
            reminder.setDeskripsi(desc);

            if (reminder instanceof MeetingReminder) {
                ((MeetingReminder) reminder).setPlatform(extra);
            } else if (reminder instanceof DeadlineReminder) {
                ((DeadlineReminder) reminder).setPrioritas(extra);
            }
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
