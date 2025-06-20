// File: Reminder.java
package Pengingat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Dijadikan abstract class
public abstract class Reminder implements Displayable {

    private String nama;
    private String waktu;
    private String deskripsi;
    private String tanggal = "";
    private boolean sudahDilewati = false;

    public Reminder(String nama, String waktu, String deskripsi) {
        this.nama = nama;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
    }

    public Reminder(String nama, String waktu, String deskripsi, String tanggal) {
        this.nama = nama;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
    }

    public boolean isSudahDilewati() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date reminderTime = sdf.parse(this.tanggal + " " + this.waktu);
            Date now = new Date();
            this.sudahDilewati = now.after(reminderTime);
            return this.sudahDilewati;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean getStatusSelesai() {
        return this.sudahDilewati;
    }

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

    // Diubah jadi abstract → wajib diimplementasikan oleh subclass
    @Override
    public abstract String display();
}
