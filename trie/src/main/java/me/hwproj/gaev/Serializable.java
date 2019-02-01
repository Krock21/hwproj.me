package me.hwproj.gaev;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

interface Serializable {
    void serialize(@NotNull OutputStream out) throws IOException;

    void deserialize(@NotNull InputStream in) throws IOException;
}
