module inoks.java.utils {
    requires transitive inoks.java.monads;

    requires java.logging;
    requires java.xml;
    requires annotations;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.joda;
    requires com.fasterxml.jackson.datatype.jsr310;

    exports de.kgrupp.inoksjavautils.data;
    exports de.kgrupp.inoksjavautils.io;
    exports de.kgrupp.inoksjavautils.resbundle;
    exports de.kgrupp.inoksjavautils.transform;
}