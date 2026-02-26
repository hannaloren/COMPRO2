public class SmartDevice {
    private String deviceName;
    private boolean isOn;

    public SmartDevice(String deviceName, boolean isOn) {
        this.deviceName = deviceName;
        this.isOn = isOn;
        isOn = false;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean isOn) {
        this.isOn = isOn;
    }

    public void togglePower(){
        char power = 0;
        switch (power){
            case 1 :
                isOn = true;
                break;
            case 0 :
                isOn = false;
                break;
        }
    }

    public String displayStatus(){
       return "Device Name: " + deviceName + "-" + isOn;
    }

   

}
