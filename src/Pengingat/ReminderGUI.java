// File: ReminderGUI.java
package Pengingat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReminderGUI extends JFrame {
    private ReminderManager reminderManager;
    private JPanel mainPanel;

    public ReminderGUI(ReminderManager reminderManager) {
        this.reminderManager = reminderManager;

        setTitle("Pengingat Jadwal");
        setSize(450, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(240, 240, 255));

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(240, 240, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("+ Tambah Kegiatan");
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(176, 196, 222));
        addButton.setForeground(new Color(40, 75, 99));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        addButton.addActionListener(this::tambahKegiatan);
        add(addButton, BorderLayout.SOUTH);

        tampilkanReminders();
    }

    private void tambahKegiatan(ActionEvent e) {
    String title = "";
    String time = "";
    String date = "";
    String desc = "";

    boolean inputValid = false;

    while (!inputValid) {
        JTextField titleField = new JTextField(title);
        JTextField timeField = new JTextField(time);
        JTextField dateField = new JTextField(date);
        JTextField descField = new JTextField(desc);

        JLabel charCounter = new JLabel(desc.length() + "/120");
        charCounter.setForeground(Color.GRAY);
        charCounter.setFont(new Font("Arial", Font.ITALIC, 10));

        // Real-time counter untuk deskripsi
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

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Tambah Kegiatan",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        // Ambil input terbaru
        title = titleField.getText().trim();
        time = timeField.getText().trim();
        date = dateField.getText().trim();
        desc = descField.getText().trim();

        // Validasi
        if (title.isEmpty() || time.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Judul, jam, dan tanggal tidak boleh kosong.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            continue;
        }

        if (!isValidTime(time)) {
            JOptionPane.showMessageDialog(this,
                    "Format waktu tidak valid. Gunakan format HH:mm (contoh: 14:30).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            continue;
        }

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this,
                    "Format tanggal tidak valid. Gunakan format yyyy-MM-dd (contoh: 2025-06-13).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            continue;
        }

        if (desc.length() > 120) {
            JOptionPane.showMessageDialog(this,
                    "Deskripsi tidak boleh lebih dari 120 karakter.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            continue;
        }

        // Simpan reminder
        reminderManager.tambahReminder(new Reminder(title, time, desc, date));
        tampilkanReminders();
        inputValid = true;
    }
}



    private void editReminder(Reminder reminder) {
    String title = reminder.getNama();
    String time = reminder.getWaktu();
    String date = reminder.getTanggal();
    String desc = reminder.getDeskripsi();

    boolean inputValid = false;

    while (!inputValid) {
        JTextField titleField = new JTextField(title);
        JTextField timeField = new JTextField(time);
        JTextField dateField = new JTextField(date);
        JTextField descField = new JTextField(desc);

        JLabel charCounter = new JLabel(desc.length() + "/120");
        charCounter.setForeground(Color.GRAY);
        charCounter.setFont(new Font("Arial", Font.ITALIC, 10));

        // Listener real-time untuk hitung karakter
        descField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateCount();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateCount();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateCount();
            }
            private void updateCount() {
                int len = descField.getText().length();
                charCounter.setText(len + "/120");
                if (len > 120) {
                    charCounter.setForeground(Color.RED);
                } else {
                    charCounter.setForeground(Color.GRAY);
                }
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
        formPanel.add(charCounter); // Tambahkan label karakter counter

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit Kegiatan",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        // Simpan nilai baru
        title = titleField.getText().trim();
        time = timeField.getText().trim();
        date = dateField.getText().trim();
        desc = descField.getText().trim();

        // Validasi
        if (title.isEmpty() || time.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Judul, jam, dan tanggal tidak boleh kosong.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            continue;
        }

        if (!isValidTime(time)) {
            JOptionPane.showMessageDialog(this,
                    "Format waktu tidak valid. Gunakan format HH:mm.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            continue;
        }

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this,
                    "Format tanggal tidak valid. Gunakan format yyyy-MM-dd.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            continue;
        }

        if (desc.length() > 120) {
            JOptionPane.showMessageDialog(this,
                    "Deskripsi tidak boleh lebih dari 120 karakter.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            continue;
        }

        reminder.setNama(title);
        reminder.setWaktu(time);
        reminder.setTanggal(date);
        reminder.setDeskripsi(desc);
        tampilkanReminders();
        inputValid = true;
    }
}



    private void tampilkanReminders() {
        mainPanel.removeAll();

        JLabel header = new JLabel("Daftar Kegiatan");
        header.setForeground(new Color(40, 75, 99));
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(header);
        mainPanel.add(Box.createVerticalStrut(20));

        List<Reminder> reminders = reminderManager.getDaftarReminder();
        for (Reminder r : reminders) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            panel.setBackground(new Color(224, 240, 255));
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 220)),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));

            JLabel title = new JLabel(r.getNama());
            title.setForeground(new Color(40, 75, 99));
            title.setFont(new Font("Arial", Font.BOLD, 14));
            title.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel date = new JLabel(r.getTanggal());
            date.setForeground(new Color(80, 80, 120));
            date.setFont(new Font("Arial", Font.PLAIN, 11));
            date.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea desc = new JTextArea(r.getDeskripsi());
            desc.setWrapStyleWord(true);
            desc.setLineWrap(true);
            desc.setEditable(false);
            desc.setFocusable(false);
            desc.setOpaque(false);
            desc.setForeground(new Color(100, 100, 100));
            desc.setFont(new Font("Arial", Font.ITALIC, 11));
            desc.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBackground(panel.getBackground());
            textPanel.add(title);
            textPanel.add(date);
            textPanel.add(desc);

            JLabel time = new JLabel(r.getWaktu());
            time.setForeground(new Color(60, 120, 160));
            time.setFont(new Font("Arial", Font.BOLD, 13));

            // Ikon tombol
            ImageIcon editIcon = new ImageIcon(
                new ImageIcon("assets/edit.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
            );
            ImageIcon deleteIcon = new ImageIcon(
                new ImageIcon("assets/delete.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
            );

            JButton editButton = new JButton(editIcon);
            editButton.setFocusPainted(false);
            editButton.setBackground(new Color(255, 200, 100));
            editButton.setPreferredSize(new Dimension(36, 30));
            editButton.setToolTipText("Edit kegiatan");
            editButton.addActionListener(e -> editReminder(r));

            JButton hapusButton = new JButton(deleteIcon);
            hapusButton.setFocusPainted(false);
            hapusButton.setBackground(new Color(255, 0, 0));
            hapusButton.setPreferredSize(new Dimension(36, 30));
            hapusButton.setToolTipText("Hapus kegiatan");
            hapusButton.addActionListener(e -> {
                reminderManager.hapusReminder(r);
                tampilkanReminders();
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            buttonPanel.setBackground(panel.getBackground());
            buttonPanel.add(editButton);
            buttonPanel.add(hapusButton);

            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setBackground(panel.getBackground());
            rightPanel.add(time, BorderLayout.NORTH);
            rightPanel.add(buttonPanel, BorderLayout.SOUTH);

            panel.add(textPanel, BorderLayout.CENTER);
            panel.add(rightPanel, BorderLayout.EAST);

            mainPanel.add(panel);
            mainPanel.add(Box.createVerticalStrut(10));
        }

        mainPanel.revalidate();
        mainPanel.repaint();
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
}
