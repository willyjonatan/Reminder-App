package Pengingat;

public class MeetingReminder extends Reminder {

    private String platform;

    public MeetingReminder(String nama, String waktu, String deskripsi, String tanggal, String platform) {
        super(nama, waktu, deskripsi, tanggal);
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String display() {
        return "Judul: " + getNama() +
               "\nWaktu: " + getWaktu() +
               "\nTanggal: " + getTanggal() +
               "\nDeskripsi: " + getDeskripsi() +
               "\nPlatform: " + platform;
    }
}
