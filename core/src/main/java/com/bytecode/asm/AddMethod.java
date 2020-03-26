package com.bytecode.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-09-29 10:55
 **/
public class AddMethod extends ClassVisitor implements Opcodes {

    public static void main(String[] args) throws Exception {
        String output = "/Users/xiaoyiluo/Documents/my-space/code/test/core/target/classes/com/bytecode/newi";
        String classDir = output + "/ObjectHelper.class";
        ClassReader classReader = new ClassReader(new FileInputStream(classDir));
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        // 预览生成的类是不是想要的类
        TraceClassVisitor tcv = new TraceClassVisitor(classWriter, new ASMifier(), new PrintWriter(System.out));
        /**
         * ClassWriter 类并不会核实对其方法的调用顺序是否恰当，以及参数是否有效。
         * 因此，有可能会生成一些被 Java 虚拟机验证器拒绝的无效类。为了尽可能􏰁前检测出部分此类错误，
         * 可以使用 CheckClassAdapter 类。和 TraceClassVisitor 类似，这个类也扩展了 ClassVisitor 类，
         * 并将对其方法的所有调用都委托到另一个 ClassVisitor，比如一个 TraceClassVisitor 或一个 ClassWriter。
         * 但是，这个类并不会打印所访问类的文本表示， 而是验证其对方法的调用顺序是否适当，参数是否有效，
         * 然后才会委托给下一个访问器。当发生 错误时，会抛出 IllegalStateException 或 IllegalArgumentException。
         */
        CheckClassAdapter checkClassAdapter = new CheckClassAdapter(tcv);
        ClassVisitor addFieldVisitor = new AddMethod(Opcodes.ASM6, checkClassAdapter);
        classReader.accept(addFieldVisitor, ClassReader.EXPAND_FRAMES);
        byte[] newClass = classWriter.toByteArray();
        File newFile = new File(output, "ObjectHelper.class");
        new FileOutputStream(newFile).write(newClass);

        Class<?> clazz = Class.forName("com.bytecode.newi.ObjectHelper");
    }

    private static String PERFMA_PRE = "$$perfma_wrapper$$_";
    private MethodDes nativeMethod;
    private String internalClazzName;

    /**
     * Constructs a new {@link ClassVisitor}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}, {@link Opcodes#ASM5} or {@link Opcodes#ASM6}.
     * @param cv  the class visitor to which this visitor must delegate method
     */
    public AddMethod(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        internalClazzName = name;
        super.visit(version, access, internalClazzName, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        if ((access & ACC_NATIVE) != 0) {
            System.out.println(name + " is native...");
        } else {
            System.out.println(name + " is not native...");
        }

        if ("getObjectSize".equals(name)) {
            // int ACC_NATIVE = 0x0100;
            if ((access & ACC_NATIVE) != 0) {
                nativeMethod = new MethodDes(access, name, desc, signature, exceptions);
                return null;
            }
        }

        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        if (nativeMethod != null) {
            String newMethodName = PERFMA_PRE + nativeMethod.name;
            System.out.println("new method: " + newMethodName);
            super.visitMethod(nativeMethod.access & ~ACC_PUBLIC | ACC_PRIVATE, newMethodName,
                    nativeMethod.desc, nativeMethod.signature, nativeMethod.exceptions);

            MethodVisitor mv = super.visitMethod(nativeMethod.access & ~ACC_NATIVE, nativeMethod.name,
                    nativeMethod.desc, nativeMethod.signature, nativeMethod.exceptions);
            mv = new MethodModifyVisitor(this.api, mv, nativeMethod, internalClazzName);
            mv.visitCode();
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        super.visitEnd();
    }

    static class MethodModifyVisitor extends AdviceAdapter {
        private MethodDes methodDes;
        private String internalClazzName;
        private Type[] argumentTypes;

        public MethodModifyVisitor(int api, MethodVisitor mv, MethodDes des, String internalClazzName) {
            super(api, mv, des.access, des.name, des.desc);
            methodDes = des;
            this.internalClazzName = internalClazzName;
            this.argumentTypes = Type.getArgumentTypes(methodDes.desc);
        }

        @Override
        public void visitCode() {
            super.visitCode();

            // println msg
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("hello native injection...");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            int instruction = INVOKEVIRTUAL;
            if ((methodDes.access & ACC_STATIC) != 0) {
                instruction = INVOKESTATIC;
            } else {
                loadThis();
            }

            // 准备参数
            loadArgs();
            mv.visitMethodInsn(instruction, internalClazzName, PERFMA_PRE + methodDes.name,
                    methodDes.desc, false);

            // 返回值处理
            Type returnType = Type.getReturnType(methodDes.desc);
            mv.visitInsn(returnType.getOpcode(Opcodes.IRETURN));
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + this.argumentTypes.length + 1,
                    maxLocals + this.argumentTypes.length + 1);
        }
    }

    static class MethodDes {

        MethodDes(int access, String name, String desc, String signature, String[] exceptions) {
            this.access = access;
            this.name = name;
            this.desc = desc;
            this.signature = signature;
            this.exceptions = exceptions;
        }

        int access;
        String name;
        String desc;
        String signature;
        String[] exceptions;
    }
}
