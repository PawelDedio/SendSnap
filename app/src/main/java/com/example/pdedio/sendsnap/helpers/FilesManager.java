package com.example.pdedio.sendsnap.helpers;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by p.dedio on 21.12.16.
 */
@EBean
public class FilesManager {

    public void copyFile(File source, File destination) throws IOException {
        FileInputStream in = new FileInputStream(source);
        FileOutputStream out = new FileOutputStream(destination);
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        inChannel.transferTo(0, inChannel.size(), outChannel);
        in.close();
        out.close();
    }
}
