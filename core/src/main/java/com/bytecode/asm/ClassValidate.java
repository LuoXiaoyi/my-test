package com.bytecode.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.FileInputStream;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-12-23 17:28
 **/
public class ClassValidate {

    public static void main(String[] args) throws Exception {
        final String targetClz = "/Users/xiaoyiluo/Desktop/HelloController.class";
        ClassReader classReader = new ClassReader(new FileInputStream(targetClz));
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        CheckClassAdapter checkClassAdapter = new CheckClassAdapter(classWriter);
        classReader.accept(checkClassAdapter, ClassReader.EXPAND_FRAMES);
        classWriter.toByteArray();
    }
}
