package com.perfma;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-06-05 18:37
 **/
@MyAnnotation(id = "xiluo")
public class AnnotationTest {

    String getId() {
        Class<?> myClass = this.getClass();
        MyAnnotation myAnno = myClass.getAnnotation(MyAnnotation.class);
        return myAnno.id();
    }

    public static void main(String[] args) {
        AnnotationTest test = new AnnotationTest();
        System.out.println(test.getId());

        A a = new A();
        System.out.println(a.getId());

        String c = null;
        String b = " " + c;
        System.out.println(b);
    }

}

@MyAnnotation(id = "xiluo2")
class A extends AnnotationTest{
}
