package com;

import jline.console.ConsoleReader;

import java.io.IOException;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/4/23 14:04
 * @Version 1.0
 **/
public class ShellTest2 {
    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));

        ConsoleReader consoleReader = new ConsoleReader();
        consoleReader.setPrompt("[PerfMa Doraemon] $ ");
        while(true) {
            String abc = consoleReader.readLine();
            if("exit".equals(abc)) break;
        }
    }
}

