public class Main {
    public static void main(String[] args) {
        SmartLight LivingRoomLight = new SmartLight("Panasonic", false,80);
        SmartLight LivingRoomLight1 = new SmartLight("Panasonic", true,30);
        LivingRoomLight.displayStatus();
        LivingRoomLight1.displayStatus();
    }
}
