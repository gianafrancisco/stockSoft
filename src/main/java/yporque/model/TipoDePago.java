package yporque.model;

/**
 * Created by francisco on 13/12/2015.
 */
public enum TipoDePago {
    EFECTIVO("Efectivo"),
    TARJETA("Tarjeta"),
    MIXTO("Mixto");

    private final String type;

    TipoDePago(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
