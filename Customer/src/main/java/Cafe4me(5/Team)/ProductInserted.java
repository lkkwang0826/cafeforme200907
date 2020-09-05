
package Cafe4me(5.Team);

public class ProductInserted extends AbstractEvent {

    private Long id;
    private Long ProductID;
    private Integer Price;
    private String Status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return ProductID;
    }

    public void setProductId(Long ProductID) {
        this.ProductID = ProductID;
    }
    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer Price) {
        this.Price = Price;
    }
    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
}
