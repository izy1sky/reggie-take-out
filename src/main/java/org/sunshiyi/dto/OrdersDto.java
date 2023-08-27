package org.sunshiyi.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sunshiyi.entity.OrderDetail;
import org.sunshiyi.entity.Orders;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
