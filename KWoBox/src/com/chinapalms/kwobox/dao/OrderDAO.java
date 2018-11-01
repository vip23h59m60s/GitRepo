package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.Order;

public interface OrderDAO {

    public boolean addOrder(Order order);

    public boolean updateOrder(Order order);

    public boolean updateOrderInfo(Order order);

    public Order findOrderByOrderId(String orderId);

    public List<Order> findOrdersByPhoneNumber(String phoneNumber);

    public List<Order> findOrdersByPhoneNumberAndPayState(String phoneNumber,
            int payState);

    public List<Order> findOrdersByPhoneNumberAndPayStestAndPageNumber(
            String phoneNumber, int payState, int pageNumber);

    public int findOrderCountByPhoneNumberAndPayState(String phoneNumber,
            int payState);

    public int findOrderCountByPhoneNumber(String phoneNumber);

    public List<Order> findTopNOrders(int topNOrderNumbers);

    public int findCurrentDayOrderCount();

    public int findTotalOrderCount();

    public Order findAllActualPayTotalAndAllTotalFavourableByPhoneNumber(
            String phoneNumber);

}
