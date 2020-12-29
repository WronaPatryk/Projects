package pl.edu.pw.gis.services;

import pl.edu.pw.gis.dto.Order;

import java.util.List;

public interface OrdersStorage {
    List<Order> getOrders();
    boolean addOrder(Order order);
    boolean deleteOrder(int orderId);
    boolean putOrder(int orderId, Order order);
}
