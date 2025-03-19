import static java.lang.Thread.currentThread;

public class Elevator extends Station {

    private boolean atTop; // Assume elevator starts at the top

    public Elevator() {
        super(-1); // Elevator has a station id 0
        this.atTop = true;
    }

    public synchronized void move() {
        // Empty Lift - Lift operator will move lift at random intervals
        while(cart == null) {
            try {
                wait(Params.operatorPause());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Lift not empty, but new cart is waiting for engine
        while(!atTop && cart.gems == 0) {
            try {
                wait(Params.operatorPause());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Lift not empty, full cart is waiting for consumer
        while(atTop && cart.gems == Params.STATIONS) {
            try {
                wait(Params.operatorPause());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            // Get direction
            String movement = "ascends";
            if(atTop) {
                movement = "descends";
            }

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

    // Used by the "consumer" class to take the full cart from the lift
    public synchronized Cart depart() {
        // Elevator must have a cart
        // Elevator must be on top
        // Cart must be full
        while(this.cart == null || !atTop || this.cart.gems == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Cart tmp = this.cart;
        this.cart = null;
        notifyAll();
        return tmp;
    }

    // Used by the "engine" class to take the empty cart from the lift
    public synchronized Cart engineDepart() {
        // Elevator must have a cart
        // Elevator must be on bottom
        // Cart must be empty
        while(this.cart == null || atTop || this.cart.gems == Params.STATIONS) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Cart tmp = this.cart;
        this.cart = null;
        notifyAll();
        return tmp;
    }

    public synchronized void arrive(Cart c) {
//        while(c.gems == 0 && !atTop) { // At bottom and new cart, don't go up
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        while(c.gems == Params.STATIONS && atTop) { // At top and cart got gems, don't go down
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }

        super.arrive(c);
        notifyAll();
    }
}
