package com.bytecode.newi;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.FileInputStream;
import java.io.PrintWriter;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-09 10:52
 **/
public class PrintASMBySrc {
    public static void main(String[] args) throws Exception {
        String output = "/Users/xiaoyiluo/Documents/my-space/code/test/core/target/classes/com/bytecode/newi";
        String classDir = output + "/ObjectHelper.class";
        ClassReader classReader = new ClassReader(new FileInputStream(classDir));
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        // 通过 ASMifier 可以将 java 源码，逆转成 asm 的方法，从而可以帮助使用 asm 来生成对应的字节码
        TraceClassVisitor tcv = new TraceClassVisitor(classWriter, new ASMifier(), new PrintWriter(System.out));
        classReader.accept(tcv, ClassReader.EXPAND_FRAMES);
    }
}
