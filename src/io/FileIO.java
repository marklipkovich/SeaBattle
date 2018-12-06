package io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class FileIO {

    public void save(File file, StoreObject storeObj) throws IOException {
        OutputStream fileStream = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(fileStream);
        os.writeObject(storeObj);
    }

    public StoreObject load (File file) throws IOException, ClassNotFoundException {
        StoreObject loadObj;
        InputStream fileStream = new FileInputStream(file);
        ObjectInputStream os = new ObjectInputStream(fileStream);
        loadObj = (StoreObject) os.readObject();
        return loadObj;
    }
}
