package com.bytecode.share;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-10-23 10:06
 **/
public class ShareMain {
    public static void main(String[] args) throws Exception {
        final String targetClazz = "/Users/xiaoyiluo/Documents/my-space/code/test/core/target/classes/com/bytecode/share/Test.class";
        ClassReader classReader = new ClassReader(new FileInputStream(targetClazz));
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

        // 预览生成的类是不是想要的类
        TraceClassVisitor tcv = new TraceClassVisitor(classWriter, new ASMifier(), new PrintWriter(System.out));

        // 添加我们的转换逻辑
        TraceClassVisitorAdapter traceClassVisitor = new TraceClassVisitorAdapter(tcv);
        // 添加校验，确保生成的字节码是有效的
        CheckClassAdapter checkClassAdapter = new CheckClassAdapter(traceClassVisitor);

        classReader.accept(checkClassAdapter, ClassReader.EXPAND_FRAMES);

        // 转换之后的结果
        byte[] modifiedBytecode = classWriter.toByteArray();
        File newFile = new File(targetClazz);
        new FileOutputStream(newFile).write(modifiedBytecode);

        Class<?> clazz = Class.forName("com.bytecode.share.Test");
        Method execute = clazz.getMethod("execute");
        execute.invoke(null);
    }
}
