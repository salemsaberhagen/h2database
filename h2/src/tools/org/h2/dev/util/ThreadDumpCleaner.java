/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.dev.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * A tool that removes uninteresting lines from stack traces.
 */
public class ThreadDumpCleaner {
    
    private static final String[] PATTERN = {
        "\\$\\$YJP\\$\\$",
        "\"(Attach|Service|VM|GC|DestroyJavaVM|Signal|AWT|AppKit|C2 |" + 
                "process reaper|YJPAgent-).*?\"(?s).*?\n\n",
        "   Locked ownable synchronizers:(?s).*?\n\n",
        "\".*?\".*?\n   java.lang.Thread.State: (TIMED_)?WAITING(?s).*?\n\n",
        "\".*?\".*?\n   java.lang.Thread.State:.*\n\t" + 
                "at sun.nio.ch.KQueueArrayWrapper.kevent0(?s).*?\n\n",
        "\".*?\".*?\n   java.lang.Thread.State:.*\n\t" + 
                "at java.io.FileInputStream.readBytes(?s).*?\n\n",
        "\".*?\".*?\n   java.lang.Thread.State:.*\n\t" + 
                "at sun.nio.ch.ServerSocketChannelImpl.accept(?s).*?\n\n",
        "JNI global references:.*\n\n",
    };
    
    private ArrayList<Pattern> patterns = new ArrayList<Pattern>();
    
    {
        for (String s : PATTERN) {
            patterns.add(Pattern.compile(s));
        }
    }

    /**
     * Run the tool.
     *
     * @param args the command line arguments
     */
    public static void main(String... args) throws IOException {
        FileReader r = new FileReader(args[0]);
        LineNumberReader in = new LineNumberReader(new BufferedReader(r));
        new ThreadDumpCleaner().run(in);
    }
    
    private void run(LineNumberReader in) throws IOException {
        StringBuilder buff = new StringBuilder();
        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            buff.append(line).append('\n');
            if (line.length() == 0) {
                System.out.print(filter(buff.toString()));
                buff = new StringBuilder();
            }
        }
        System.out.print(filter(buff.toString()));
    }        
    
    private String filter(String s) {
        for (Pattern p : patterns) {
            s = p.matcher(s).replaceAll("");
        }
        return s;
    }
    
}