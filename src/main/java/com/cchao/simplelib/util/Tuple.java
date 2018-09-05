package com.cchao.simplelib.util;

/**
 * @author cchao
 * @version 7/8/18.
 */
public class Tuple {
    public static <A, B> Tuple2 tuple(A a, B b) {
        return new Tuple2<>(a, b);
    }

    public static <A, B, C> Tuple3 tuple(A a, B b, C c) {
        return new Tuple3<>(a, b, c);
    }

    public static <A, B, C, D> Tuple4 tuple(A a, B b, C c, D d) {
        return new Tuple4<>(a, b, c, d);
    }

    public static class Tuple2<A,B>{
        public final A a;
        public final B b;

        public Tuple2(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

    public static class Tuple3<A, B, C> extends Tuple2<A, B> {
        public final C c;

        public Tuple3(A a, B b, C c) {
            super(a, b);
            this.c = c;
        }
    }

    public static class Tuple4<A, B, C, D> extends Tuple3<A, B, C> {
        public final D d;

        public Tuple4(A a, B b, C c, D d) {
            super(a, b, c);
            this.d = d;
        }
    }
}
