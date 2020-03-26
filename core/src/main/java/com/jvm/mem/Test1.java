package com.jvm.mem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author xiluo
 * @createTime 2019/4/11 23:42
 **/
public class Test1 {
    static class X{
        protected void a(){

        }
    }

    static class B extends X{
        String name;

        @Override
        public void a() {
            super.a();
        }

        @Override
        public String toString() {
            return "B{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    static class A implements Cloneable{
        String name;
        List<String> ls;
        B b;

        @Override
        public String toString() {
            return "A{" +
                    "name='" + name + '\'' +
                    ", ls=" + ls +
                    ", B=" + b +
                    '}';
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        List<String> ls = new ArrayList<>();
        ls.add("2332");

        A a = new A();
        a.ls = ls;
        a.b = new B();
        a.b.name = "xiluo";
        System.out.println(a);

        A b = (A) a.clone();
        System.out.println(b);
    }

}
