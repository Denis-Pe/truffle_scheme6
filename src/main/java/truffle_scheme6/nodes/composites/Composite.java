package truffle_scheme6.nodes.composites;


import truffle_scheme6.SchemeObject;

public abstract class Composite extends SchemeObject {
    abstract Iterable<SchemeObject> children();
}
