package com.jvm.methodinvoke;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/5/18 20:31
 * @Version 1.0
 **/
public class InvokeTest {
    B b = new B();

    public static void main(String[] args) {
        // invokeDynamic
        I i = M::world;
        // invokeInterface
        i.hello();

        // invokeSpecial
        I  a = new A();
        // invokeInterface
        a.hello();
        // invokeStatic
        ((A)a).say();

        // invokeVirtual
        ((A)a).bye();

        InvokeTest t = new InvokeTest();
        // invokeStatic
        t.b.say();

        // invokeVirtual
        t.abc();
    }

    void abc(){

    }

    private class B {
        private void say(){
            System.out.println("say");
        }
    }

    public static class A implements I{
        @Override
        public void hello() {
            System.out.println("hello");
        }

        private void say(){
            System.out.println("say");
        }

        public final void bye(){
            System.out.println("bye");
        }
    }

    public interface I {
        void hello();
    }

    public static class M {
        static void world() {
            System.out.println("world");
        }
    }

}
