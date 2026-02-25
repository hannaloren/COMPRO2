import java.util.*;

public class App {
    public static void main(String[] args) {
        Food fo1 = new Food ("Fruit", "Banana", 90.5);
        System.out.println(fo1.getInfo());
        Fruit f1 = new Fruit("Fruit", "Apple", 35.5, "Red");
        System.out.println(f1.getInfo());

        List <String> ingredients = new ArrayList<>();
        ingredients.add("flour");
        ingredients.add("egg");
        ingredients.add("sugar");
        ingredients.add("food coloring");

        Pastries p1 = new Pastries("Pastry", "Cake", 280.55, "Sweet", ingredients );
        System.out.println(p1.getInfo());

    }
}
