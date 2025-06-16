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

        // Timer otomatis untuk refresh setiap 10 detik
        Timer autoRefresh = new Timer(1_000, e -> {
            reminderManager.checkReminders(); // Tampilkan notifikasi jika waktunya tiba
            tampilkanReminders();            // Perbarui tampilan (warna & label)
        });
        autoRefresh.start();
    }

    private void tambahKegiatan(ActionEvent e) {
        ReminderFormDialog dialog = new ReminderFormDialog(this, null, reminderManager);
        dialog.setVisible(true);

        Reminder newReminder = dialog.getReminder();
        if (newReminder != null) {
            reminderManager.tambahReminder(newReminder);
            tampilkanReminders();
        }
    }

    private void editReminder(Reminder reminder) {
        ReminderFormDialog dialog = new ReminderFormDialog(this, reminder, reminderManager);
        dialog.setVisible(true);

        Reminder updatedReminder = dialog.getReminder();
        if (updatedReminder != null) {
            reminderManager.hapusReminder(reminder);
            reminderManager.tambahReminder(updatedReminder);
            tampilkanReminders();
        }
    }

    public void tampilkanReminders() {
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
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));

            if (r.isSudahDilewati()) {
                panel.setBackground(new Color(220, 255, 220)); // hijau muda
            } else {
                panel.setBackground(new Color(224, 240, 255));
            }

            JLabel title = new JLabel(r.getNama());
            title.setForeground(new Color(40, 75, 99));
            title.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel date = new JLabel(r.getTanggal() + " | " + r.getWaktu());
            date.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            date.setForeground(new Color(0, 120, 215));
            date.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

            JTextArea desc = new JTextArea("• " + r.getDeskripsi());
            desc.setWrapStyleWord(true);
            desc.setLineWrap(true);
            desc.setEditable(false);
            desc.setFocusable(false);
            desc.setOpaque(false);
            desc.setForeground(new Color(100, 100, 100));
            desc.setFont(new Font("Arial", Font.ITALIC, 11));

            String extraInfo = "";
            if (r instanceof MeetingReminder) {
                extraInfo = "Platform: " + ((MeetingReminder) r).getPlatform();
            } else if (r instanceof DeadlineReminder) {
                extraInfo = "Prioritas: " + ((DeadlineReminder) r).getPrioritas();
            }

            JLabel extraLabel = new JLabel(extraInfo);
            extraLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            extraLabel.setForeground(new Color(80, 80, 120));
            extraLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

            // ...
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBackground(panel.getBackground());
            textPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // pastikan seluruh panel kiri

            // SET LEFT ALIGN UNTUK SEMUA KOMPONEN DI TEXTPANEL
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            date.setAlignmentX(Component.LEFT_ALIGNMENT);
            desc.setAlignmentX(Component.LEFT_ALIGNMENT);
            extraLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            textPanel.add(title);
            textPanel.add(date);
            textPanel.add(desc);

            if (!extraInfo.isEmpty()) {
                textPanel.add(extraLabel);
            }

            // Tambahkan label status jika sudah dilewati
            if (r.isSudahDilewati()) {
                ImageIcon checkIcon = new ImageIcon(
                    new ImageIcon("assets/check.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
                );
                JLabel checkLabel = new JLabel(" Kegiatan Telah Selesai / Terlewati", checkIcon, JLabel.LEFT);
                checkLabel.setFont(new Font("Arial", Font.BOLD, 12));
                checkLabel.setForeground(new Color(34, 139, 34));
                checkLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
                checkLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // ini penting agar label status align kiri
                textPanel.add(checkLabel);
            }


            ImageIcon editIcon = new ImageIcon(
                new ImageIcon("assets/edit.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
            );
            JButton editButton = new JButton(editIcon);
            editButton.setFocusPainted(false);
            editButton.setBackground(new Color(255, 200, 100));
            editButton.setPreferredSize(new Dimension(36, 30));
            editButton.setToolTipText("Edit kegiatan");
            editButton.addActionListener(e -> editReminder(r));

            ImageIcon deleteIcon = new ImageIcon(
                new ImageIcon("assets/delete.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
            );
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
            rightPanel.add(buttonPanel, BorderLayout.SOUTH);

            panel.add(textPanel, BorderLayout.CENTER);
            panel.add(rightPanel, BorderLayout.EAST);

            mainPanel.add(panel);
            mainPanel.add(Box.createVerticalStrut(10));
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
