// File: Reminder.java
package Pengingat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Kelas Reminder mengimplementasikan interface Displayable (POLYMORPHISM via INTERFACE)
public class Reminder implements Displayable {

    // [Private Fields] → encapsulated (ENCAPSULATION)
    private String nama;          // Judul kegiatan
    private String waktu;         // Waktu kegiatan (HH:mm)
    private String deskripsi;     // Deskripsi kegiatan
    private String tanggal = "";  // Tanggal kegiatan (yyyy-MM-dd)
    private boolean sudahDilewati = false; // Status apakah kegiatan telah terlewati

    // [Constructor] Tanpa tanggal (OVERLOADED)
    public Reminder(String nama, String waktu, String deskripsi) {
        this.nama = nama;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
    }

    // [Constructor] Dengan tanggal (OVERLOADED)
    public Reminder(String nama, String waktu, String deskripsi, String tanggal) {
        this.nama = nama;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
    }

    // [Behavior Method] Mengecek apakah waktu reminder sudah lewat (ABSTRACTION + LOGIC)
    public boolean isSudahDilewati() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date reminderTime = sdf.parse(this.tanggal + " " + this.waktu);
            Date now = new Date();

            this.sudahDilewati = now.after(reminderTime); // simpan hasil untuk akses selanjutnya
            return this.sudahDilewati;
        } catch (ParseException e) {
            return false;
        }
    }

    // [Getter Method]
    public boolean getStatusSelesai() {
        return this.sudahDilewati;
    }

    // [Getter & Setter Methods] (ENCAPSULATION)
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    // [Polymorphism] Implementasi dari Interface Displayable (ABSTRACTION)
    @Override
    public String display() {
        return "Judul: " + nama +
               "\nWaktu: " + waktu +
               "\nTanggal: " + tanggal +
               "\nDeskripsi: " + deskripsi;
    }
}
