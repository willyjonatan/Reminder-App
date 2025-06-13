// File: ReminderGUI.java
package Pengingat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        JTextField titleField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField descField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(0, 1));
        formPanel.setBackground(new Color(240, 240, 255));
        formPanel.add(new JLabel("Judul:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Jam (HH:MM):"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Deskripsi:"));
        formPanel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Tambah Kegiatan",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String time = timeField.getText().trim();
            String date = dateField.getText().trim();
            String desc = descField.getText().trim();

            if (!title.isEmpty() && !time.isEmpty() && !date.isEmpty()) {
                reminderManager.tambahReminder(new Reminder(title, time, desc, date));
                tampilkanReminders();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Judul, jam, dan tanggal tidak boleh kosong.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
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

            JButton hapusButton = new JButton("🗑");
            hapusButton.setFocusPainted(false);
            hapusButton.setBackground(new Color(2, 100, 100));
            hapusButton.setForeground(Color.WHITE);
            hapusButton.setFont(new Font("Arial", Font.BOLD, 6));
            hapusButton.addActionListener(e -> {
                reminderManager.hapusReminder(r);
                tampilkanReminders();
            });

            JLabel title = new JLabel(r.getNama());
            title.setForeground(new Color(40, 75, 99));
            title.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel date = new JLabel(r.getTanggal());
            date.setForeground(new Color(80, 80, 120));
            date.setFont(new Font("Arial", Font.PLAIN, 11));

            JLabel desc = new JLabel(r.getDeskripsi());
            desc.setForeground(new Color(100, 100, 100));
            desc.setFont(new Font("Arial", Font.ITALIC, 11));

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBackground(panel.getBackground());
            textPanel.add(title);
            textPanel.add(date);
            textPanel.add(desc);

            JLabel time = new JLabel(r.getWaktu());
            time.setForeground(new Color(60, 120, 160));
            time.setFont(new Font("Arial", Font.BOLD, 13));

            panel.add(hapusButton, BorderLayout.WEST);
            panel.add(textPanel, BorderLayout.CENTER);
            panel.add(time, BorderLayout.EAST);

            mainPanel.add(panel);
            mainPanel.add(Box.createVerticalStrut(10));
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
