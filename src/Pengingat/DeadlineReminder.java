// File: DeadlineReminder.java
package Pengingat;

public class DeadlineReminder extends Reminder {
    private String prioritas;

    public DeadlineReminder(String nama, String waktu, String deskripsi, String tanggal, String prioritas) {
        super(nama, waktu, deskripsi, tanggal);
        this.prioritas = (prioritas != null && !prioritas.isEmpty()) ? prioritas : "Normal";
    }

    public String getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(String prioritas) {
        this.prioritas = prioritas;
    }

    @Override
    public String display() {
        return super.display() + "\nPrioritas: " + prioritas;
    }
} 
