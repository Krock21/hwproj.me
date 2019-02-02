package me.hwproj.gaev;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializable {
    /**
     * serializes this object
     *
     * @param out OutputStream, where object will be serialized
     * @throws IOException if Exception in OutputStream
     */
    void serialize(@NotNull OutputStream out) throws IOException;

    /**
     * deserializes this object
     *
     * @param in InputStream, from where object will be deserialized
     * @throws IOException if Exception in InputStream
     */
    void deserialize(@NotNull InputStream in) throws IOException;
}
