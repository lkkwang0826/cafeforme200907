package local;

public class OrderReceived extends AbstractEvent {

    private Long id;

    public OrderReceived(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
