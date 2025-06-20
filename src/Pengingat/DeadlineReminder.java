// File: DeadlineReminder.java
package Pengingat;

// Kelas DeadlineReminder merupakan subclass dari Reminder (INHERITANCE)
public class DeadlineReminder extends Reminder {

    // Variabel instance (FIELD) dengan access modifier 'private' (ENCAPSULATION)
    private String prioritas;

    // Constructor dengan parameter, memanggil constructor superclass (super) (CONSTRUCTOR)
    public DeadlineReminder(String nama, String waktu, String deskripsi, String tanggal, String prioritas) {
        super(nama, waktu, deskripsi, tanggal); // Memanggil konstruktor Reminder (INHERITANCE)
        
        // Validasi nilai prioritas, jika null/empty maka default "Normal"
        this.prioritas = (prioritas != null && !prioritas.isEmpty()) ? prioritas : "Normal";
    }

    // Getter method untuk prioritas (ENCAPSULATION)
    public String getPrioritas() {
        return prioritas;
    }

    // Setter method untuk prioritas (ENCAPSULATION)
    public void setPrioritas(String prioritas) {
        this.prioritas = prioritas;
    }

    // Override method display() dari superclass Reminder (POLYMORPHISM)
    @Override
    public String display() {
        // Tidak lagi memanggil super.display() karena Reminder.display() adalah abstract
        return "Judul: " + getNama() +
               "\nWaktu: " + getWaktu() +
               "\nTanggal: " + getTanggal() +
               "\nDeskripsi: " + getDeskripsi() +
               "\nPrioritas: " + prioritas;
    }
}
