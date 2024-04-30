package truffle_scheme6.nodes.composites;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SNilLiteralNode;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.SPair;

import java.util.InputMismatchException;
import java.util.StringJoiner;

public class SListNode extends SchemeNode {
    @Child
    private SchemeNode form;
    @Children
    private SchemeNode[] args; // may be nil-terminated if it was coded as such

    public SListNode(SchemeNode form, SchemeNode[] args) {
        this.form = form;

        if (args.length == 0) {
            throw new RuntimeException("Attempted to build list with empty args. This is not possible");
        }

        this.args = args;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        // todo
        return null;
    }

    @Override
    public SPair executeFrozen(VirtualFrame frame) {
        SPair ret = new SPair(null, args[args.length - 1]);

        for (int i = args.length - 2; i >= 0; i--) {
            ret.setCar(args[i].executeFrozen(frame));

            ret = new SPair(null, ret);
        }

        ret.setCar(form.executeFrozen(frame));

        return ret;
    }

    public SchemeNode getForm() {
        return form;
    }

    public SchemeNode[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return this.toStringList();
    }

    public String toStringList() {
        var joiner = new StringJoiner(" ");

        joiner.add(form.toString());

        if (args[args.length - 1] instanceof SNilLiteralNode) {
            for (int i = 0; i < args.length - 1; i++) {
                joiner.add(args[i].toString());
            }
        } else {
            for (int i = 0; i < args.length - 1; i++) {
                joiner.add(args[i].toString());
            }
            joiner.add(".");
            joiner.add(args[args.length - 1].toString());
        }

        return "(" + joiner + ")";
    }

    public String toStringPairs() {
        var builder = new StringBuilder();

        if (args.length > 1) {
            builder.append('(');
            builder.append(args[args.length - 2]);
            builder.append(" . ");
            builder.append(args[args.length - 1]);
            builder.append(')');

            for (int i = args.length - 3; i >= 0; i--) {
                builder.append(')');
                builder.insert(0, " . ");
                builder.insert(0, args[i]);
                builder.insert(0, '(');
            }

            builder.append(')');
            builder.insert(0, " . ");
            builder.insert(0, form);
            builder.insert(0, '(');
        } else {
            builder.append('(');
            builder.append(form);
            builder.append(" . ");
            builder.append(args[args.length - 1]);
            builder.append(')');
        }

        return builder.toString();
    }
}
