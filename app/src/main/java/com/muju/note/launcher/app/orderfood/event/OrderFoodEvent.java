package com.muju.note.launcher.app.orderfood.event;

public class OrderFoodEvent {
    public double price;

    public OrderFoodEvent(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
