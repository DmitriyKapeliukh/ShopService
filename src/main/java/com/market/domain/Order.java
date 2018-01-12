package com.market.domain;

import java.util.List;

public class Order {

    private final User user;
    private final List<Item> orderedItems;

    public Order(User user, List<Item> orderedItems) {
        this.user = user;
        this.orderedItems = orderedItems;
    }

    public boolean userHasEnoughBalance() {
        return (this.user.getBalance() > countTotalOrdersCost());
    }

    public double countTotalOrdersCost() {
        return this.orderedItems.stream()
                .mapToDouble(Item::getCost)
                .sum();
    }

    public User getUser() {
        return user;
    }

    public List<Item> getOrderedItems() {
        return orderedItems;
    }
}
