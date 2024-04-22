package p.denis.composites;


import p.denis.SchemeObject;

public abstract class Composite extends SchemeObject {
    abstract Iterable<SchemeObject> children();
}
