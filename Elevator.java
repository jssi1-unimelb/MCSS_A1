import static java.lang.Thread.currentThread;

public class Elevator extends Station {

    private boolean atTop; // Assume elevator starts at the top

    public Elevator() {
        super(-1); // Elevator has a station id 0
        this.atTop = true;
    }

    public synchronized void move() {
        // Get direction
        String movement = "ascends";
        if(atTop) {
            movement = "descends";
        }

        // Empty Lift
        while(cart == null) {
            try {
                wait(Params.operatorPause());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            if(this.cart == null) {
                System.out.println("elevator " + movement + " (empty)");
            }
            System.out.println("elevator " + movement + " with " + cart);
            wait(Params.ELEVATOR_TIME);
            this.atTop = !atTop;
            notifyAll(); // wake up engine waiting to deliver cart
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public @Override synchronized Cart depart() {
        while(this.cart == null) { // Elevator must have a cart to depart
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(atTop && cart.gems == 0) { // At top and
            while(atTop) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else { // Elevator at Bottom
            while(!atTop) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Cart tmp = this.cart;
        this.cart = null;
        notifyAll();
        return tmp;
    }

    public @Override synchronized void arrive(Cart c) {
        while(c.gems == 0 && !atTop) { // At bottom and new cart, don't go up
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        while(c.gems == Params.STATIONS && atTop) { // At top and cart got gems, don't go down
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        super.arrive(c);
        notifyAll();
    }
}
