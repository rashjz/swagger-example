package rashjz.info.app.sw.controllers;

import com.google.common.io.Files;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rashjz.info.app.sw.domain.Person;
import rashjz.info.app.sw.domain.Product;
import rashjz.info.app.sw.respositories.PersonRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api")
@Api(value = "product-details", description = "Api Operations of application")
public class HomeController {
    private final PersonRepository personRepository;

    @Autowired
    public HomeController(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @ApiOperation(value = "View a list of available products", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")

    })
    @GetMapping("/product/{productId}")
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
        log.info("product saved {}", product.toString());
        return product;
    }

    @ApiOperation(value = "View a list of available products", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")

    })
    @GetMapping("/permission/")
    public String getFolderPermission()  {
        try {
            return Files.readFirstLine(new File("/etc/default/grub"), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
