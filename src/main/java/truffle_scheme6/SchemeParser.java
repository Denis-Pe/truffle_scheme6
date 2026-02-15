package truffle_scheme6;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import com.oracle.truffle.api.source.Source;
import truffle_scheme6.nodes.roots.SchemeRoot;

public interface SchemeParser {
    SchemeRoot parseRoot(SchemeLanguage language, Source input);

    SchemeParser clojureImpl = new SchemeParser() {
        final IFn clojureParseFn;

        {
            IFn req = Clojure.var("clojure.core", "require");
            req.invoke(Clojure.read("truffle-scheme6.java-exports"));
            clojureParseFn = Clojure.var("truffle-scheme6.java-exports", "parse");
        }

        @Override
        public SchemeRoot parseRoot(SchemeLanguage language, Source input) {
            return (SchemeRoot) clojureParseFn.invoke(language, input);
        }
    };
}
