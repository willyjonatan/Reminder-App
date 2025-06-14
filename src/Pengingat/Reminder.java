package Pengingat;

interface Displayable {
    String display();
}

// Kelas Reminder sebagai representasi pengingat biasa
public class Reminder implements Displayable {
    private String nama;
    private String waktu;
    private String deskripsi;
    private String tanggal = ""; // Tambahan: tanggal default kosong

    public Reminder(String nama, String waktu, String deskripsi) {
        this.nama = nama;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
    }

    // Overload constructor untuk tambahan GUI
    public Reminder(String nama, String waktu, String deskripsi, String tanggal) {
        this.nama = nama;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
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

    @Override
    public String display() {
        return toString();
    }

    @Override
    public String toString() {
        return "📅 " + (tanggal.isEmpty() ? "(Tanpa tanggal)" : tanggal) +
                " | 🕒 " + waktu + " - " + nama +
                "\n📝 " + deskripsi;
    }
}
