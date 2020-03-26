package com.jvm.dc;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * test
 *
 * @author xiluo
 * @createTime 2020-01-10 19:37
 **/
public class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final Map<String, byte[]> classBytes = new HashMap<>();
    private ClassLoader classLoader;
    private PackageInternalsFinder finder;

    protected MemoryJavaFileManager(JavaFileManager fileManager, ClassLoader classLoader,
                                    PackageInternalsFinder finder) {
        super(fileManager);
        this.classLoader = classLoader;
        this.finder = finder;
    }

    public JavaFileObject makeStringSource(String fileName, String javaSource) {
        return new MemoryInputJavaFileObject(fileName, javaSource);
    }

    public Map<String, byte[]> getClassBytes() {
        return new HashMap<>(classBytes);
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof CustomJavaFileObject) {
            // TODO
        } else {
            return this.fileManager.inferBinaryName(location, file);
        }

        return null;
    }

    @Override
    public void close() throws IOException {
        super.close();
        classBytes.clear();
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className,
                                               JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            return new MemoryOutputJavaFileObject(className);
        }

        return super.getJavaFileForOutput(location, className, kind, sibling);
    }

    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName,
                                         Set<JavaFileObject.Kind> kinds,
                                         boolean recurse) throws IOException {
        if (location == StandardLocation.PLATFORM_CLASS_PATH) {
            return fileManager.list(location, packageName, kinds, recurse);
        } else if (location == StandardLocation.CLASS_OUTPUT && kinds.contains(JavaFileObject.Kind.CLASS)) {
            return finder.find(packageName);
        }

        return new LinkedHashSet<>();
    }

    static class MemoryInputJavaFileObject extends SimpleJavaFileObject {
        String code;

        MemoryInputJavaFileObject(String name, String code) {
            super(URI.create("string:///" + name), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return CharBuffer.wrap(code);
        }
    }

    class MemoryOutputJavaFileObject extends SimpleJavaFileObject {
        String name;

        MemoryOutputJavaFileObject(String name) {
            super(URI.create("string:///" + name), Kind.SOURCE);
            this.name = name;
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return new FilterOutputStream(new ByteArrayOutputStream()) {
                @Override
                public void close() throws IOException {
                    out.close();
                    ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                    classBytes.put(name, bos.toByteArray());
                }
            };
        }
    }

}
