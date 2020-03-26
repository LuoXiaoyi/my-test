package com.bytecode.asm;

import org.objectweb.asm.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author xiluo
 * @createTime 2019/1/9 15:13
 **/
public class AddField extends ClassVisitor {

    String name;
    int access;
    String desc;
    Object value;

    boolean duplicate;

    public AddField(int api, ClassVisitor cv, String name, int access, String desc, Object value) {
        super(api, cv);
        this.name = name;
        this.access = access;
        this.desc = desc;
        this.value = value;
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println("visit field : " + name);
        if (this.name.equals(name)) {
            duplicate = true;
        }
        return cv.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!duplicate) {
            FieldVisitor fv = super.visitField(access, name, desc, null, value);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }

    public static void main(String[] args) throws Exception {
        String output = "/Users/xiaoyiluo/IdeaProjects/test/target/classes/com/bytecode/asm/";
        String classDir = "/Users/xiaoyiluo/IdeaProjects/test/target/classes/com/bytecode/asm/AddField.class";
        ClassReader classReader = new ClassReader(new FileInputStream(classDir));
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor addFieldVisitor = new AddField(Opcodes.ASM6, classWriter,
                "field",
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL,
                Type.getDescriptor(String.class),
                "value"
        );
        classReader.accept(addFieldVisitor, ClassReader.EXPAND_FRAMES);
        byte[] newClass = classWriter.toByteArray();
        File newFile = new File(output, "AddFieldNew.class");
        new FileOutputStream(newFile).write(newClass);
    }

}
