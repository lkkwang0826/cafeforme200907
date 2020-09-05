package Cafe4me(5.Team);

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="OrderStatus_table")
public class OrderStatus {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

}
