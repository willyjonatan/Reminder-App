// File: MeetingReminder.java
package Pengingat;

// Kelas MeetingReminder adalah subclass dari Reminder (INHERITANCE)
public class MeetingReminder extends Reminder {

    // Variabel instance (FIELD) dengan access modifier 'private' (ENCAPSULATION)
    private String platform;

    // Constructor MeetingReminder (CONSTRUCTOR), memanggil konstruktor superclass Reminder
    public MeetingReminder(String nama, String waktu, String deskripsi, String tanggal, String platform) {
        super(nama, waktu, deskripsi, tanggal); // Memanggil constructor dari Reminder (INHERITANCE)
        this.platform = platform;
    }

    // Getter method untuk variabel platform (ENCAPSULATION)
    public String getPlatform() {
        return platform;
    }

    // Setter method untuk variabel platform (ENCAPSULATION)
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    // Override method display() dari superclass Reminder (POLYMORPHISM)
    @Override
    public String display() {
        // Menambahkan info platform ke hasil display Reminder (POLYMORPHISM + INHERITANCE)
        return super.display() + "\nPlatform: " + platform;
    }
}
