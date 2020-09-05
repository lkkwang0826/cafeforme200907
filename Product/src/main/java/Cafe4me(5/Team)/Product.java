package Cafe4me(5.Team);

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Product_table")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long productId;
    private Integer price;
    private String status;

    @PrePersist
    public void onPrePersist(){
        ProductInserted productInserted = new ProductInserted();
        BeanUtils.copyProperties(this, productInserted);
        productInserted.publishAfterCommit();


        ProductDeleted productDeleted = new ProductDeleted();
        BeanUtils.copyProperties(this, productDeleted);
        productDeleted.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
