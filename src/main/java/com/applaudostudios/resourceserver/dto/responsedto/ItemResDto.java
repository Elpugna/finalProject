package com.applaudostudios.resourceserver.dto.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ItemResDto {
  private String name;
  private double price;
  private long quantity;
  private long id;

}
