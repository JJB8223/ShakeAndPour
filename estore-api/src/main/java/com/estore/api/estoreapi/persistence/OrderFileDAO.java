package com.estore.api.estoreapi.persistence;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Kit;
import com.estore.api.estoreapi.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Implements functionality for JSON filed-based persistence for order histories
 * 
 * @author Duncan French
 */
@Component
public class OrderFileDAO implements OrderDAO {

    private Map<Integer, Order> orderHistory; // local cache of all Orders in the store's order history
                                              // the key is an Order's id, the value is the Order with that id
    
    private ObjectMapper objectMapper; // Provides conversion between Order objects and JSON text 
                                       // format written to the file
    private static int nextId; // the next ID to assign to a new Order
    private String filename; // filename to read and write from
    /**
     * This constructor creates a new OrderFileDAO 
     * 
     * @param filename This is the file that the DAO will read from and write to
     * @param objectMapper This is the object mapper that will be used to convert 
     * between Order objects and JSON text
     * 
     * @throws IOException Thrown if something is wrong with the underlying storage
     */
    public OrderFileDAO(@Value("${orders.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename; 
        this.objectMapper = objectMapper;
        load();
    }
    
    /**
     * This method initializes the orderHistory map by reading from the file path initialized in the constructor
     * 
     * @return Returns true if no exceptions are thrown, indicating data was loaded from the file successfully
     * 
     * @throws IOException Thrown if something is wrong with the underlying storage
     */
    private boolean load() throws IOException {
        orderHistory = new TreeMap<>();
        nextId = 0;
        // Deserializes the JSON objects from the file into an array of products
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Order[] OrderArray = objectMapper.readValue(new File(filename),Order[].class);
        // Add each Order to the tree map and keep track of the greatest id
        for (Order order : OrderArray) {
            orderHistory.put(order.getId(), order);
            if (order.getId() > nextId)
                nextId = order.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }
    /**
     * Generates the next id for a new {@linkplain Order order}
     *
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }
    /**
     * Generates an array of {@linkplain Order orders} from the tree map for any
     * {@linkplain Order orders} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Order orders}
     * in the tree map
     *
     * @return  The array of {@link Order orders}, may be empty
     */
    private Order[] getOrdersArray(String containsText) { // if containsText == null, no filter
        ArrayList<Order> orderHistoryArrayList = new ArrayList<>();
        for (Order order : orderHistory.values()){
            if (containsText == null || order.containsMatchingKit(containsText)){
                orderHistoryArrayList.add(order);
            }
        }
        Order[] orderList  = new Order[orderHistoryArrayList.size()];
        orderHistoryArrayList.toArray(orderList);
        return orderList;
    }
    /**
     * Gets an array of {@linkplain Order orders} from the getOrdersArray method that 
     * doesn't specify a username, then filters by the provided username
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Order orders} 
     * matching the specified username
     * 
     * @return The array of {@link Order orders}, may be empty
     */
    private Order[] getOrdersArray(String containsText, String username) {
        Order[] unfilteredOrders = getOrdersArray(containsText);
        ArrayList<Order> orderHistoryArrayList = new ArrayList<>();
        for (Order order : unfilteredOrders) {
            if (order.purchasedBy(username)) {
                orderHistoryArrayList.add(order);
            }
        }
        Order[] filteredOrders = new Order[orderHistoryArrayList.size()];
        orderHistoryArrayList.toArray(filteredOrders);
        return filteredOrders;
    }
    /**
     * Saves the {@linkplain Order orders} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link Order orders} were written successfully
     *
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Order[] orders = getOrdersArray(null);
        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), orders);
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Order createOrder(String username, ArrayList<Kit> kits) throws IOException {
        synchronized(orderHistory) {
            // create new order object, assign the next unique id to it
            Order newOrder = new Order(nextId(), username, kits);
            orderHistory.put(newOrder.getId(), newOrder);
            save();
            return newOrder;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Order[] findOrders(String containsText, String username) throws IOException {
        synchronized(orderHistory) {
            return getOrdersArray(containsText, username);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Order getOrder(int id) {
        synchronized(orderHistory) {
            return orderHistory.get(id);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Order[] getOrders(String username) throws IOException {
        synchronized(orderHistory) {
            return getOrdersArray(null, username);
        }
    }
}