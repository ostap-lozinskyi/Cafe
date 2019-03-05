package ua.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "place", indexes = @Index(columnList = "name", unique = true))
public class Place extends AbstractEntityName {

    private int countOfPeople;

    private boolean isFree;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Place() {
    }

    public Place(int countOfPeople) {
        this.countOfPeople = countOfPeople;
    }

    @OneToMany(mappedBy = "place")
    private List<Order> orders = new ArrayList<>();

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }

    public int getCountOfPeople() {
        return countOfPeople;
    }

    public void setCountOfPeople(int countOfPeople) {
        this.countOfPeople = countOfPeople;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
