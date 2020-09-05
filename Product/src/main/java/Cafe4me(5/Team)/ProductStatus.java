package Cafe4me(5.Team);

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="ProductStatus_table")
public class ProductStatus {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String productId;
        private String productStatus;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
        public String getProductStatus() {
            return productStatus;
        }

        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
        }

}
