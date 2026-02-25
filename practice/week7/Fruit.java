public class Fruit extends Food {
        private String color;
        
        public Fruit(){

        }

        public Fruit(String color) {
            this.color = color;
        }

        public Fruit(String type, String name, double price, String color) {
            super(type, name, price);
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }


        @Override
        public String getInfo(){
            return "Fruit: " +super.getType() +" - "+   super.getName() + " - " + super.getPrice() + " - " + color; 
        }

    }

