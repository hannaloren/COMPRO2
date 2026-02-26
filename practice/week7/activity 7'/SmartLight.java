public class SmartLight extends SmartDevice {
    private int brightness;
    public SmartLight(String deviceName, boolean isOn, int brightness) {
        super(deviceName, isOn);
        this.brightness = brightness;
    }

     public int getBrightness() {
        return brightness;
    }
    
    public void setBrightness(int level) {
        if (isOn() == true)
        this.brightness = level;  
        }
        
    @Override
    public String displayStatus(){
        return "SmartLight: " + super.getDeviceName() + "-" + super.isOn() + "-" + brightness;
    }
    
}
