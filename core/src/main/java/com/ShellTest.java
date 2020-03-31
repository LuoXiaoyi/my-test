package com;

import org.fusesource.jansi.Ansi;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/3/31 19:07
 * @Version 1.0
 **/
public class ShellTest {
    static final String PREFIX = "[Perfma Doraemon]$ ";

    public static void main(String[] args) throws IOException {
        //System.out.println("\\e[1;33m This is yellow text! \\e[0m");
        /*TerminalBuilder terminalBuilder = TerminalBuilder.builder();
        Terminal terminal = terminalBuilder.build();
        terminal.writer().println("hello world.");*/
        commandLine();

    }

    static void ansi() {
        System.out.print(Ansi.ansi().eraseScreen().fg(Ansi.Color.RED).a(PREFIX + "\n").reset());
        System.out.print(Ansi.ansi().eraseScreen().fg(Ansi.Color.YELLOW).a("Hello").reset());
        System.out.print(Ansi.ansi().eraseScreen().fg(Ansi.Color.BLUE).a(" World").reset());
    }

    static void commandLine() throws IOException {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();
        while (true) {
            String line;
            try {
                line = lineReader.readLine(PREFIX);
                //System.out.print(Ansi.ansi().eraseScreen().fg(Ansi.Color.BLUE).a(line).a("\n").reset());
                BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(line).getInputStream()));
                while ((line = br.readLine()) != null) {
                    terminal.writer().print(Ansi.ansi().eraseScreen().fg(Ansi.Color.BLUE).a(line).a("\n").reset());
                }
            } catch (UserInterruptException e) {
                // Do nothing
            } catch (EndOfFileException e) {
                System.out.println("\nBye.");
                return;
            }
        }

    }
}
