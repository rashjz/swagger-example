package rashjz.info.app.sw.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import rashjz.info.app.sw.domain.Product;

import java.math.BigDecimal;
@Slf4j
@RestController
@Api(value = "product-details", description = "Operations pertaining to products")
public class HomeController {

    @GetMapping("/private/get-details")
    public String getAppDetails() {
        return "get-details";
    }


    @ApiOperation(value = "View a list of available products", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")

    })
    @GetMapping("/product/get-details/{productId}")
    public Product getAppProductDetails(@PathVariable(name = "productId") BigDecimal productId) {
        return Product.builder()
                .productId(productId)
                .productName("testName")
                .productOwner("testOwner")
                .build();
    }



    @ApiOperation(value = "Save product in database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully saved"),
            @ApiResponse(code = 401, message = "Bad resources"),
    })
    @PostMapping("/product/save")
    public Product saveProduct(@RequestBody Product product) {
      log.info("product saved {}",product.toString());
      return product;
    }
}
