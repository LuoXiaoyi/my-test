package com.cpu;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/4/5 01:05
 * @Version 1.0
 **/
public class CpuUsageCalTest {

    public static void main(String[] args) {

        long tu0 = 1648118 + 4310526 + 2488216 + 294579200 + 40047 + 439049 + 12144;
        long tu1 = 1648124 + 4310862 + 2488334 + 294579285 + 40047 + 439083 + 12144;
        long deltaTu = tu1 - tu0;

        long pu = 4681413 - 4681052;
        long ps = 2118730 - 2118579;

        long mu = 1648124 - 1648118;
        long ms = 2488334 - 2488216;
        long mn = 4310862 - 4310526;
        long mirq = 439083 - 439049;
        long midle = 294579285 - 294579200;

        System.out.println("p: u=" + (double)pu/(double)deltaTu);
        System.out.println("p: s=" + (double)ps/(double)deltaTu);
        System.out.println("m: u=" + (double)mu/(double)deltaTu);
        System.out.println("m: n=" + (double)mn/(double)deltaTu);
        System.out.println("m: s=" + (double)ms/(double)deltaTu);
        System.out.println("m: irq=" + (double)mirq/(double)deltaTu);
        System.out.println("m: idle=" + (double)midle/(double)deltaTu);

    }
}
