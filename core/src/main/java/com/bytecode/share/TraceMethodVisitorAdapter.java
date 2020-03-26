package com.bytecode.share;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-23 14:22
 **/
public class TraceMethodVisitorAdapter extends AdviceAdapter implements Opcodes {
    TraceMethodVisitorAdapter(final int api,
                              final MethodVisitor methodVisitor,
                              final int access,
                              final String name,
                              final String descriptor){
        super(ASM7, methodVisitor, access, name, descriptor);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        mv.visitMethodInsn(INVOKESTATIC, "com/bytecode/share/Listener",
                "onMethodEnter", "()V", false);
    }

    @Override
    protected void onMethodExit(int opcode) {
        mv.visitMethodInsn(INVOKESTATIC, "com/bytecode/share/Listener",
                "onMethodExit", "()V", false);
        super.onMethodExit(opcode);
    }
}
