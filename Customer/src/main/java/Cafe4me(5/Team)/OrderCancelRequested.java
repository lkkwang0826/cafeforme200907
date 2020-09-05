package Cafe4me(5.Team);

public class OrderCancelRequested extends AbstractEvent {

    private Long id;

    public OrderCancelRequested(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
