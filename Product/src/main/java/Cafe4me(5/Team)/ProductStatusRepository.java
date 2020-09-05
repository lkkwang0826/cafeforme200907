package Cafe4me(5.Team);

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductStatusRepository extends CrudRepository<ProductStatus, Long> {


        void deleteByProductId(String productId);
}