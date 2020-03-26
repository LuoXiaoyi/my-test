package com.jvm.dc;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.URI;

/**
 * test
 *
 * @author xiluo
 * @createTime 2020-01-10 20:04
 **/
public class CustomJavaFileObject implements JavaFileObject {
    CustomJavaFileObject(String binaryName, URI uri) {

    }

    /**
     * Gets the kind of this file object.
     *
     * @return the kind
     */
    @Override
    public Kind getKind() {
        return null;
    }

    /**
     * Checks if this file object is compatible with the specified
     * simple name and kind.  A simple name is a single identifier
     * (not qualified) as defined in
     * <cite>The Java&trade; Language Specification</cite>,
     * section 6.2 "Names and Identifiers".
     *
     * @param simpleName a simple name of a class
     * @param kind       a kind
     * @return {@code true} if this file object is compatible; false
     * otherwise
     */
    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return false;
    }

    /**
     * Provides a hint about the nesting level of the class
     * represented by this file object.  This method may return
     * {@link NestingKind#MEMBER} to mean
     * {@link NestingKind#LOCAL} or {@link NestingKind#ANONYMOUS}.
     * If the nesting level is not known or this file object does not
     * represent a class file this method returns {@code null}.
     *
     * @return the nesting kind, or {@code null} if the nesting kind
     * is not known
     */
    @Override
    public NestingKind getNestingKind() {
        return null;
    }

    /**
     * Provides a hint about the access level of the class represented
     * by this file object.  If the access level is not known or if
     * this file object does not represent a class file this method
     * returns {@code null}.
     *
     * @return the access level
     */
    @Override
    public Modifier getAccessLevel() {
        return null;
    }

    /**
     * Returns a URI identifying this file object.
     *
     * @return a URI
     */
    @Override
    public URI toUri() {
        return null;
    }

    /**
     * Gets a user-friendly name for this file object.  The exact
     * value returned is not specified but implementations should take
     * care to preserve names as given by the user.  For example, if
     * the user writes the filename {@code "BobsApp\Test.java"} on
     * the command line, this method should return {@code
     * "BobsApp\Test.java"} whereas the {@linkplain #toUri toUri}
     * method might return {@code
     * file:///C:/Documents%20and%20Settings/UncleBob/BobsApp/Test.java}.
     *
     * @return a user-friendly name
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Gets an InputStream for this file object.
     *
     * @return an InputStream
     * @throws IllegalStateException         if this file object was
     *                                       opened for writing and does not support reading
     * @throws UnsupportedOperationException if this kind of file
     *                                       object does not support byte access
     * @throws IOException                   if an I/O error occurred
     */
    @Override
    public InputStream openInputStream() throws IOException {
        return null;
    }

    /**
     * Gets an OutputStream for this file object.
     *
     * @return an OutputStream
     * @throws IllegalStateException         if this file object was
     *                                       opened for reading and does not support writing
     * @throws UnsupportedOperationException if this kind of
     *                                       file object does not support byte access
     * @throws IOException                   if an I/O error occurred
     */
    @Override
    public OutputStream openOutputStream() throws IOException {
        return null;
    }

    /**
     * Gets a reader for this object.  The returned reader will
     * replace bytes that cannot be decoded with the default
     * translation character.  In addition, the reader may report a
     * diagnostic unless {@code ignoreEncodingErrors} is true.
     *
     * @param ignoreEncodingErrors ignore encoding errors if true
     * @return a Reader
     * @throws IllegalStateException         if this file object was
     *                                       opened for writing and does not support reading
     * @throws UnsupportedOperationException if this kind of
     *                                       file object does not support character access
     * @throws IOException                   if an I/O error occurred
     */
    @Override
    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        return null;
    }

    /**
     * Gets the character content of this file object, if available.
     * Any byte that cannot be decoded will be replaced by the default
     * translation character.  In addition, a diagnostic may be
     * reported unless {@code ignoreEncodingErrors} is true.
     *
     * @param ignoreEncodingErrors ignore encoding errors if true
     * @return a CharSequence if available; {@code null} otherwise
     * @throws IllegalStateException         if this file object was
     *                                       opened for writing and does not support reading
     * @throws UnsupportedOperationException if this kind of
     *                                       file object does not support character access
     * @throws IOException                   if an I/O error occurred
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return null;
    }

    /**
     * Gets a Writer for this file object.
     *
     * @return a Writer
     * @throws IllegalStateException         if this file object was
     *                                       opened for reading and does not support writing
     * @throws UnsupportedOperationException if this kind of
     *                                       file object does not support character access
     * @throws IOException                   if an I/O error occurred
     */
    @Override
    public Writer openWriter() throws IOException {
        return null;
    }

    /**
     * Gets the time this file object was last modified.  The time is
     * measured in milliseconds since the epoch (00:00:00 GMT, January
     * 1, 1970).
     *
     * @return the time this file object was last modified; or 0 if
     * the file object does not exist, if an I/O error occurred, or if
     * the operation is not supported
     */
    @Override
    public long getLastModified() {
        return 0;
    }

    /**
     * Deletes this file object.  In case of errors, returns false.
     *
     * @return true if and only if this file object is successfully
     * deleted; false otherwise
     */
    @Override
    public boolean delete() {
        return false;
    }
}
