package com.example.reactor.ProjectReactor;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;

public class ResourceHandlers {

    private void close(Closeable closeable){
        try {
            closeable.close();
            System.out.println("Closed the resource");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void write(BufferedWriter bw, String string){
        try {
            bw.write(string);
            bw.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
