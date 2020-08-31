package model.hotelservice;

import java.math.BigDecimal;

public class HotelService {

    private Integer id;
    private BigDecimal price;
    private HotelServiceType type;

    public HotelService(final BigDecimal price, final HotelServiceType type) {
        this.price = price;
        this.type = type;
    }

    public HotelService(final Integer id, final BigDecimal price, final HotelServiceType type) {
        this.id = id;
        this.price = price;
        this.type = type;
    }

    public HotelService(final Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public HotelServiceType getType() {
        return type;
    }

    @Override
    public boolean equals(final Object obj) {
        if (id == null) {
            return false;
        }
        return (obj instanceof HotelService) && this.id.equals(((HotelService) obj).getId());
    }

    @Override
    public String toString() {
        return String.format("service %s, id:%d, price: %.2f", type, id, price);
    }
}

