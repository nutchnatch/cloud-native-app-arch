package org.template.demo.creditcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.template.demo.data.BaseEntity;

import javax.persistence.*;

@Data
@NoArgsConstructor
public class CreditCardDto {

 private String number;

 @Enumerated(EnumType.STRING)
 private CreditCardType type;

 public CreditCardDto(String number, CreditCardType type) {
  this.number = number;
  this.type = type;
 }
}
