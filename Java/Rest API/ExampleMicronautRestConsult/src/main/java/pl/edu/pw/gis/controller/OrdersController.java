package pl.edu.pw.gis.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import pl.edu.pw.gis.dto.Order;
import pl.edu.pw.gis.services.OrdersStorage;

import java.util.List;

@Controller("/orders")
public class OrdersController {
    public static OrdersStorage _ordersStorage;

    public OrdersController(OrdersStorage ordersStorage) {
        this._ordersStorage = ordersStorage;
    }

    @Get()
    HttpResponse<List<Order>> getOrders() {
        return HttpResponse.ok( _ordersStorage.getOrders());
    }


    @Delete("/{id}")
    HttpResponse<Order> deleteOrder(@PathVariable Integer id){
        if(_ordersStorage.deleteOrder( id )){
            return HttpResponse.ok();
        } else {
            return HttpResponse.notModified();
        }
    }
    @Put("/{id}")
    HttpResponse<Order> putOrder(@PathVariable Integer id, Order order) {
        if (_ordersStorage.putOrder( id, order )) {
            return HttpResponse.created( order );
        } else {
            return HttpResponse.badRequest();
        }
    }

    @Post()
    HttpResponse<Order> postOrder(Order order) {
        if (_ordersStorage.addOrder( order )) {
            return HttpResponse.created( order );
        } else {
            return HttpResponse.badRequest();
        }
    }
}

