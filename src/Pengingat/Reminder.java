// File: Reminder.java
package Pengingat;

// Kelas Reminder mengimplementasikan interface Displayable (INTERFACE)
public class Reminder implements Displayable {

    // Field (variabel instance) bersifat private → akses dibatasi (ENCAPSULATION)
    private String nama;       // Judul kegiatan
    private String waktu;      // Waktu kegiatan
    private String deskripsi;  // Deskripsi kegiatan
    private String tanggal = ""; // Tanggal kegiatan (default kosong)

    // Constructor pertama: tanpa parameter tanggal (CONSTRUCTOR)
    public Reminder(String nama, String waktu, String deskripsi) {
        this.nama = nama;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
    }

    // Constructor kedua: dengan parameter tanggal (CONSTRUCTOR - OVERLOADED)
    public Reminder(String nama, String waktu, String deskripsi, String tanggal) {
        this.nama = nama;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
    }

    // Getter method untuk nama (ENCAPSULATION)
    public String getNama() {
        return nama;
    }

    // Setter method untuk nama (ENCAPSULATION)
    public void setNama(String nama) {
        this.nama = nama;
    }

    // Getter method untuk waktu (ENCAPSULATION)
    public String getWaktu() {
        return waktu;
    }

    // Setter method untuk waktu (ENCAPSULATION)
    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    // Getter method untuk deskripsi (ENCAPSULATION)
    public String getDeskripsi() {
        return deskripsi;
    }

    // Setter method untuk deskripsi (ENCAPSULATION)
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    // Getter method untuk tanggal (ENCAPSULATION)
    public String getTanggal() {
        return tanggal;
    }

    // Setter method untuk tanggal (ENCAPSULATION)
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    // Implementasi method dari interface Displayable (POLYMORPHISM melalui INTERFACE)
    @Override
    public String display() {
        // Mengembalikan representasi teks dari objek Reminder (ABSTRACTION: menyembunyikan detail internal)
        return "Judul: " + nama + "\nWaktu: " + waktu + "\nTanggal: " + tanggal + "\nDeskripsi: " + deskripsi;
    }
}
