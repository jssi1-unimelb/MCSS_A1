public abstract class StationTemplate {
    protected int id;
    protected Cart cart;

    public StationTemplate(int id) {
        this.id = id;
        this.cart = null;
    }

    public abstract void stationArrive(Cart c);

    public abstract Cart stationDepart();
}
