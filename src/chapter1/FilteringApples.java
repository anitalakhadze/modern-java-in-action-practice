package chapter1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilteringApples {
    private static List<Apple> inventory = Arrays.asList(
            new Apple(80, "green"),
            new Apple(155, "green"),
            new Apple(120, "red")
    );

    public static void main(String[] args) {
        filterGreenApples(inventory).forEach(System.out::println);
        filterHeavyApples(inventory).forEach(System.out::println);

        filterApples(inventory, FilteringApples::isGreenApple).forEach(System.out::println);
        filterApples(inventory, FilteringApples::isHeavyApple).forEach(System.out::println);

        filterApples(inventory, (Apple a) -> a.getColor().equals("green")).forEach(System.out::println);
        filterApples(inventory, (Apple a) -> a.getWeight() > 150).forEach(System.out::println);
    }

    // very ineffective - creating separate clones of essentially the same code
    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor().equals("green")) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterHeavyApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() > 150) {
                result.add(apple);
            }
        }
        return result;
    }

    // method reference is not a very good way too, does not deal with the
    // behavior parameterization problem
    public static boolean isGreenApple(Apple apple) {
        return apple.getColor().equals("green");
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }

    // predicate now encapsulates the condition to test on an apple
    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> predicate) {
        return inventory
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    // abstract version of the above function
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static class Apple {
        private int weight = 0;
        private String color = "";

        public Apple(int weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @SuppressWarnings("boxing")
        @Override
        public String toString() {
            return String.format("Apple{color='%s', weight=%d}", color, weight);
        }
    }
}
