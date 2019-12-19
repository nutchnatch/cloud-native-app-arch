package org.template.demo.order;

import org.template.demo.data.BaseEntity;
import org.template.demo.address.Address;
import org.template.demo.address.AddressType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Order extends BaseEntity {

 @Id
 private String orderId;

 private String accountNumber;

 private OrderStatus orderStatus;

 private List<LineItem> lineItems = new ArrayList<>();

 private Address shippingAddress;

 public Order(String orderId, String accountNumber, Address shippingAddress) {
  this.orderId = orderId;
  this.accountNumber = accountNumber;
  this.shippingAddress = shippingAddress;
  this.shippingAddress.setAddressType(AddressType.SHIPPING);
  this.orderStatus = OrderStatus.PENDING;
 }

 public void addLineItem(LineItem lineItem) {
  this.lineItems.add(lineItem);
 }

 public void setOrderStatus(OrderStatus status) {
  this.orderStatus = status;
 }
}
