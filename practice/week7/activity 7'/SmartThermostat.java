public class SmartThermostat extends SmartDevice{
    private double temperature;
   
    public SmartThermostat(String deviceName, boolean isOn, double temperature) {
        super(deviceName, isOn);
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(double temp){
        if (temperature > 30){
            System.out.println("Warning temperature is above 30 degrees");
        }
    }

    @Override
    public String displayStatus(){
        return "Thermostat: " + super.getDeviceName() + "-" + super.isOn()+ "-" + temperature;
    }
    
}
