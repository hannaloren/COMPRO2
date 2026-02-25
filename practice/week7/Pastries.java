import java.util.*;
public class Pastries extends Food {
    private String flavor;
    private  List <String> ingredients;

    public Pastries(){

    }
    public Pastries(String flavor, String[] ingredients) {
        this.flavor = flavor;
        this.ingredients = ingredients;
    }
    public Pastries(String type, String name, double price, String flavor, String[] ingredients) {
        super(type, name, price);
        this.flavor = flavor;
        this.ingredients = ingredients;
    }
    public String getFlavor() {
        return flavor;
    }
    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }
    public String[] getIngredients() {
        return ingredients;
    }
    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String getInfo(){
        return "Pastry: " + super.getType() + " - " + super.getName() + " - " + super.getPrice() + " - " + flavor + " - " + ingredients; 
    
}
}
