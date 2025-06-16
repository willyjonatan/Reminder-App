package Pengingat;

// Inheritance: ReminderFormDialog adalah subclass dari JDialog (INHERITANCE)
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReminderFormDialog extends JDialog { // INHERITANCE dari JDialog

    // Variabel instance (FIELD) bertipe GUI component, semuanya private (ENCAPSULATION)
    private JTextField titleField;       // FIELD: input judul
    private JTextField timeField;        // FIELD: input waktu
    private JTextField dateField;        // FIELD: input tanggal
    private JTextField descField;        // FIELD: input deskripsi
    private JTextField extraField;       // FIELD: input platform/prioritas
    private JComboBox<String> typeComboBox; // FIELD: dropdown jenis kegiatan
    private JLabel charCounter;          // FIELD: penghitung karakter deskripsi
    private Reminder reminder;           // FIELD: objek Reminder yang sedang dibuat/diedit
    private ReminderManager reminderManager; // FIELD: manajer pengingat (akses data kegiatan)

    // Constructor (public) (CONSTRUCTOR) - menampilkan dialog tambah/edit kegiatan
    public ReminderFormDialog(Frame owner, Reminder reminder, ReminderManager reminderManager) {
        super(owner, reminder == null ? "Tambah Kegiatan" : "Edit Kegiatan", true); // memanggil constructor superclass (JDialog)
        this.reminder = reminder;
        this.reminderManager = reminderManager;

        // Inisialisasi tampilan GUI (ABSTRACTION: menyembunyikan detail internal dari pengguna)
        setSize(400, 400);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(new Color(240, 240, 255));
        setLayout(new BorderLayout());

        // Inisialisasi input field dengan data reminder jika mode edit
        titleField = new JTextField(reminder != null ? reminder.getNama() : "");
        timeField = new JTextField(reminder != null ? reminder.getWaktu() : "");
        dateField = new JTextField(reminder != null ? reminder.getTanggal() : "");
        descField = new JTextField(reminder != null ? reminder.getDeskripsi() : "");
        extraField = new JTextField();

        // Dropdown tipe reminder
        typeComboBox = new JComboBox<>(new String[]{"Reminder Biasa", "Meeting Reminder", "Deadline Reminder"});

        // POLYMORPHISM: pengecekan tipe objek reminder menggunakan instanceof
        if (reminder instanceof MeetingReminder) {
            typeComboBox.setSelectedIndex(1);
            extraField.setText(((MeetingReminder) reminder).getPlatform());
        } else if (reminder instanceof DeadlineReminder) {
            typeComboBox.setSelectedIndex(2);
            extraField.setText(((DeadlineReminder) reminder).getPrioritas());
        }

        // Label penghitung karakter
        charCounter = new JLabel((descField.getText().length()) + "/120");
        charCounter.setForeground(Color.GRAY);
        charCounter.setFont(new Font("Arial", Font.ITALIC, 10));

        // Listener untuk pembaruan hitungan karakter (ENCAPSULATION: penggunaan DocumentListener sebagai bagian dari GUI)
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

        // Panel form utama
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(240, 240, 255));

        // Komponen-komponen input form ditambahkan ke formPanel
        // (GUI builder: ABSTRACTION menyembunyikan kompleksitas backend)

        // [komponen form ditambahkan ke formPanel seperti label dan input, tidak perlu dijelaskan ulang]

        add(formPanel, BorderLayout.CENTER);

        // Panel tombol OK dan Batal
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Batal");

        // Listener untuk tombol OK
        okButton.addActionListener(e -> {
            if (validateInput()) {
                setVisible(false);
            }
        });

        // Listener untuk tombol Batal
        cancelButton.addActionListener(e -> setVisible(false));

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method private untuk validasi input pengguna (ABSTRACTION: detail validasi tersembunyi)
    private boolean validateInput() {
        // Mengambil nilai dari field input
        String title = titleField.getText().trim();
        String time = timeField.getText().trim();
        String date = dateField.getText().trim();
        String desc = descField.getText().trim();
        String extra = extraField.getText().trim();

        // Validasi sederhana
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

        // Cek bentrok waktu
        for (Reminder existing : reminderManager.getDaftarReminder()) {
            if (reminder != null && existing == reminder) continue;
            if (existing.getTanggal().equals(date) && existing.getWaktu().equals(time)) {
                JOptionPane.showMessageDialog(this, "Sudah ada kegiatan lain pada waktu yang sama.", "Jadwal Bentrok", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // Membuat objek reminder baru berdasarkan pilihan tipe (POLYMORPHISM: memilih subclass)
        int selectedType = typeComboBox.getSelectedIndex();
        if (reminder == null) {
            switch (selectedType) {
                case 1:
                    reminder = new MeetingReminder(title, time, desc, date, extra); // POLYMORPHISM
                    break;
                case 2:
                    reminder = new DeadlineReminder(title, time, desc, date, extra); // POLYMORPHISM
                    break;
                default:
                    reminder = new Reminder(title, time, desc, date); // Pola dasar
            }
        } else {
            // Setter methods → bagian dari ENCAPSULATION
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

    // Method untuk validasi waktu (private) (ABSTRACTION)
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

    // Method untuk validasi tanggal (private) (ABSTRACTION)
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

    // Getter public untuk mengambil reminder hasil input (ENCAPSULATION)
    public Reminder getReminder() {
        return reminder;
    }
}
