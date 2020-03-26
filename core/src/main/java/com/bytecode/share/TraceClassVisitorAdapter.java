package com.bytecode.share;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-23 14:27
 **/
public class TraceClassVisitorAdapter extends ClassVisitor implements Opcodes {

    private static String targetMethodName = "execute";

    public TraceClassVisitorAdapter(final ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        if (targetMethodName.equals(name)) {
            return new TraceMethodVisitorAdapter(api, mv, access, name, descriptor);
        } else {
            return mv;
        }
    }
}
