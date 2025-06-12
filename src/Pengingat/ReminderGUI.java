package Pengingat;

import javax.swing.*;
import java.awt.*;

public class ReminderGUI extends JFrame {
    private JTextField namaField;
    private JTextField waktuField;
    private JTextField deskripsiField;
    private JTextArea outputArea;
    ReminderManager reminderManager;

    public ReminderGUI() {
        this(new ReminderManager());
    }

    public ReminderGUI(ReminderManager reminderManager) {
        this.reminderManager = reminderManager;

        setTitle("📱 Pengingat Jadwal");
        setSize(360, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(250, 250, 255));
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(new Color(240, 245, 255));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        namaField = new JTextField();
        waktuField = new JTextField();
        deskripsiField = new JTextField();

        JLabel titleLabel = new JLabel("📝 Tambah Pengingat");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(40, 75, 99));

        inputPanel.add(titleLabel);
        inputPanel.add(Box.createVerticalStrut(10));

        inputPanel.add(new JLabel("Nama Kegiatan:"));
        inputPanel.add(namaField);
        inputPanel.add(Box.createVerticalStrut(5));
        inputPanel.add(new JLabel("Waktu (HH:mm):"));
        inputPanel.add(waktuField);
        inputPanel.add(Box.createVerticalStrut(5));
        inputPanel.add(new JLabel("Deskripsi:"));
        inputPanel.add(deskripsiField);
        inputPanel.add(Box.createVerticalStrut(10));

        JButton tambahButton = new JButton("➕ Tambah");
        tambahButton.setBackground(new Color(100, 180, 255));
        tambahButton.setForeground(Color.WHITE);
        tambahButton.setFocusPainted(false);
        tambahButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton hapusButton = new JButton("🗑 Hapus Semua");
        hapusButton.setBackground(new Color(220, 80, 80));
        hapusButton.setForeground(Color.WHITE);
        hapusButton.setFocusPainted(false);
        hapusButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputPanel.add(tambahButton);
        inputPanel.add(Box.createVerticalStrut(5));
        inputPanel.add(hapusButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(new Color(255, 255, 240));
        outputArea.setForeground(new Color(50, 50, 50));
        outputArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Daftar Pengingat"));

        tambahButton.addActionListener(e -> {
            String nama = namaField.getText().trim();
            String waktu = waktuField.getText().trim();
            String deskripsi = deskripsiField.getText().trim();

            if (nama.isEmpty() || waktu.isEmpty() || deskripsi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom!");
                return;
            }

            Reminder reminder = new Reminder(nama, waktu, deskripsi);
            reminderManager.tambahReminder(reminder);
            updateTampilan();
            namaField.setText("");
            waktuField.setText("");
            deskripsiField.setText("");
        });

        hapusButton.addActionListener(e -> {
            reminderManager.hapusSemuaReminder();
            updateTampilan();
        });

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        updateTampilan();
    }

    private void updateTampilan() {
        outputArea.setText("");
        for (Reminder r : reminderManager.getDaftarReminder()) {
            outputArea.append(r.display() + "\n\n");
        }
        outputArea.append("📌 Total pengingat: " + ReminderManager.ReminderStats.hitungTotal(reminderManager));
    }
}
