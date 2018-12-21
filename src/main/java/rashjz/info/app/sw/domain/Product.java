package rashjz.info.app.sw.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable{
    @ApiModelProperty(notes = "The database generated product ID" ,required = true)
    private BigDecimal productId;
    @ApiModelProperty(notes = "The product name" ,required = true)
    private String productName;
    @ApiModelProperty(notes = "The product owner" ,required = true)
    private String productOwner;

}
