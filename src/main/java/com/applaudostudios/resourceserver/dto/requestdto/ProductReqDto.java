package com.applaudostudios.resourceserver.dto.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class ProductReqDto {
  @NotNull( message = "The product Id must be in the request body" )
  @Positive( message = "The productId must be provided" )
  private Long id;
  @NotNull( message = "The product quantity  must be in the request body" )
  @Min( value = 0L, message = "The quantity must be greater or equal than 0")
  private Long quantity;

  public ProductReqDto( Long id, Long quantity ) {
    this.id = id;
    this.quantity = quantity;
  }

}
