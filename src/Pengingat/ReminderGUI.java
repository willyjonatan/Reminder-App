package Pengingat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// Kelas ReminderGUI merupakan subclass dari JFrame (INHERITANCE dari kelas abstrak JFrame)
public class ReminderGUI extends JFrame {

    // Access modifier: private (ENCAPSULATION - membatasi akses langsung)
    // Instance variable (atribut milik objek)
    private ReminderManager reminderManager;
    private JPanel mainPanel;

    // Constructor (konstruktor ReminderGUI)
    public ReminderGUI(ReminderManager reminderManager) {
        this.reminderManager = reminderManager;

        // Method turunan dari JFrame (INHERITANCE)
        setTitle("Pengingat Jadwal");
        setSize(450, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Konfigurasi tampilan
        getContentPane().setBackground(new Color(240, 240, 255));

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(240, 240, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Button tambah kegiatan
        JButton addButton = new JButton("+ Tambah Kegiatan");
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(176, 196, 222));
        addButton.setForeground(new Color(40, 75, 99));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Event listener untuk button
        addButton.addActionListener(this::tambahKegiatan);  // POLYMORPHISM: method `addActionListener` menerima objek dengan interface `ActionListener`
        add(addButton, BorderLayout.SOUTH);

        // Menampilkan daftar kegiatan
        tampilkanReminders();
    }

    // Method dengan access modifier private (ENCAPSULATION)
    // Method untuk menambah kegiatan
    private void tambahKegiatan(ActionEvent e) {
        // Memanggil form dialog
        ReminderFormDialog dialog = new ReminderFormDialog(this, null, reminderManager);  // ABSTRACTION: pengguna tidak tahu detail implementasi form
        dialog.setVisible(true);

        Reminder newReminder = dialog.getReminder();  // ABSTRACTION: hanya memanggil interface dari dialog
        if (newReminder != null) {
            reminderManager.tambahReminder(newReminder);
            tampilkanReminders();
        }
    }

    // Method private: mengedit kegiatan
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

    // Method private: menampilkan daftar kegiatan
    private void tampilkanReminders() {
        mainPanel.removeAll();

        // Header daftar kegiatan
        JLabel header = new JLabel("Daftar Kegiatan");
        header.setForeground(new Color(40, 75, 99));
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(header);
        mainPanel.add(Box.createVerticalStrut(20));

        // Mendapatkan daftar reminder dari ReminderManager
        List<Reminder> reminders = reminderManager.getDaftarReminder();  // ABSTRACTION dan ENCAPSULATION

        for (Reminder r : reminders) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
            panel.setBackground(new Color(224, 240, 255));
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 220)),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));

            // Judul kegiatan
            JLabel title = new JLabel(r.getNama());  // ENCAPSULATION: akses melalui getter
            title.setForeground(new Color(40, 75, 99));
            title.setFont(new Font("Arial", Font.BOLD, 14));
            title.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Tanggal dan waktu kegiatan
            JLabel date = new JLabel(r.getTanggal() + " | " + r.getWaktu());  // ENCAPSULATION
            date.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            date.setForeground(new Color(0, 120, 215));
            date.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

            // Deskripsi kegiatan
            JTextArea desc = new JTextArea("• " + r.getDeskripsi());  // ENCAPSULATION
            desc.setWrapStyleWord(true);
            desc.setLineWrap(true);
            desc.setEditable(false);
            desc.setFocusable(false);
            desc.setOpaque(false);
            desc.setForeground(new Color(100, 100, 100));
            desc.setFont(new Font("Arial", Font.ITALIC, 11));
            desc.setAlignmentX(Component.LEFT_ALIGNMENT);

            // POLYMORPHISM: r bisa berupa MeetingReminder atau DeadlineReminder
            String extraInfo = "";
            if (r instanceof MeetingReminder) {
                extraInfo = "Platform: " + ((MeetingReminder) r).getPlatform();  // POLYMORPHISM: method spesifik subclass
            } else if (r instanceof DeadlineReminder) {
                extraInfo = "Prioritas: " + ((DeadlineReminder) r).getPrioritas();
            }

            JLabel extraLabel = new JLabel(extraInfo);
            extraLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            extraLabel.setForeground(new Color(80, 80, 120));
            extraLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBackground(panel.getBackground());
            textPanel.add(title);
            textPanel.add(date);
            textPanel.add(desc);
            if (!extraInfo.isEmpty()) textPanel.add(extraLabel);

            // Tombol edit
            ImageIcon editIcon = new ImageIcon(
                    new ImageIcon("assets/edit.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
            );
            JButton editButton = new JButton(editIcon);
            editButton.setFocusPainted(false);
            editButton.setBackground(new Color(255, 200, 100));
            editButton.setPreferredSize(new Dimension(36, 30));
            editButton.setToolTipText("Edit kegiatan");
            editButton.addActionListener(e -> editReminder(r));

            // Tombol hapus
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

        // Menyegarkan tampilan
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
