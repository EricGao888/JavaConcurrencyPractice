package com.ericgao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.ericgao.ProducerAndConsumer.EOF;

public class ProducerAndConsumer {
    public static final String EOF = "EOF";
    public static void main(String[] args) {
        List<String> products = new ArrayList<>();
        new Thread(new Producer(products, Color.ANSI_RED)).start();
        new Thread(new Consumer(products, Color.ANSI_CYAN)).start();
        new Thread(new Consumer(products, Color.ANSI_PURPLE)).start();
    }

}

class Producer implements Runnable {
    private List<String> products;
    private String color;

    public Producer (List<String> products, String color) {
        this.products = products;
        this.color = color;
    }

    @Override
    public void run() {
        Random random = new Random();
        String[] meals = {"Sandwich", "Hamburger", "Pizza", "Salad", "Steak", "Roasted Chicken"};
        for (String meal : meals) {
            synchronized (products) {
                System.out.println(color + "Producing product: " + meal + "...");
                products.add(meal);
            }
            try {
                Thread.sleep(random.nextInt(3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (products) {
            products.add(EOF);
            System.out.println(color + "All products have been produced and producer is off duty! ");
        }
    }
}

class Consumer implements Runnable {
    private List<String> products;
    private String color;

    public Consumer(List<String> products, String color) {
        this.products = products;
        this.color = color;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            synchronized (products) {
                if (products.isEmpty()) continue;
                if (EOF.equals(products.get(0))) {
                    System.out.println(color + "Exiting...");
                    break;
                }
                else {
                    System.out.println(color + "Consuming product: " + products.remove(0) + "...");
                }
            }
            try {
                Thread.sleep(random.nextInt(5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
