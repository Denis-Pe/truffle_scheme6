package truffle_scheme6.runtime;

public class SPair {
    private Object car;
    private Object cdr;

    // if given an array with 1 element only, will automatically nil-terminate it
    // if there are more elements, it's up to the caller to end it with nil
    public static SPair from(Object... objects) {
        if (objects.length == 0) {
            throw new IllegalArgumentException("Can't create pair from empty array");
        } else if (objects.length == 1) {
            return new SPair(objects[0], SNil.SINGLETON);
        } else {
            var curr = new SPair(null, objects[objects.length - 1]);

            for (int i = objects.length - 2; i >= 0; i--) {
                curr.setCar(objects[i]);
                curr = new SPair(null, curr);
            }

            return curr;
        }
    }

    public SPair(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public Object getCar() {
        return car;
    }

    public Object getCdr() {
        return cdr;
    }

    public void setCar(Object car) {
        this.car = car;
    }

    public void setCdr(Object cdr) {
        this.cdr = cdr;
    }

    @Override
    public String toString() {
        return "(" + car + " . " + cdr + ")";
    }
}
