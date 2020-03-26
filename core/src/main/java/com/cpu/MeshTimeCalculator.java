package com.cpu;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-30 14:11
 **/
public class MeshTimeCalculator {

    public static void main(String[] args) {
        long a = 1559311756801L;
        System.out.println(TimeAlignUtil.align(a, 5000));

        long cms = System.currentTimeMillis();
        System.out.println("cms: " + format(cms));
        long sms = cms - 4800000;

        System.out.println("sms: " + format(sms));
        long ems = cms - 812349;

        System.out.println("ems: " + format(ems));
        Map<Integer, List<Long>> result = new HashMap<>();
        calculate(sms, ems, cms, 1800000, result);

        Set<Map.Entry<Integer, List<Long>>> entrys = result.entrySet();
        for (Map.Entry<Integer, List<Long>> entry : entrys) {
            System.out.println("entry: " + entry.getKey());
            for (Long ts : entry.getValue()) {
                System.out.println("\t" + format(ts));
            }
            System.out.println("");
        }
    }

    public static void calculate(long startMs, long endMs, long currentMs, int meshMs, Map<Integer, List<Long>> result) {
        int nextMeshMs = nextMesh(meshMs);

        if (endMs - startMs > meshMs) {
            long startMsAlign = TimeAlignUtil.align(startMs, meshMs);
            long endMsAlign = TimeAlignUtil.align(endMs, meshMs);

            if (meshMs != 5000 && startMs != startMsAlign) {
                startMsAlign += meshMs;
            }

            if (meshMs == 5000) {
                endMsAlign += meshMs;
            }

            if (startMsAlign < endMsAlign) {
                int delayMs = 1000;
                for (long m = startMsAlign;
                     m < endMsAlign && currentMs - meshMs - delayMs >= endMsAlign;
                     m += meshMs) {
                    List<Long> ts = result.get(meshMs);
                    if (ts == null) {
                        ts = new LinkedList<>();
                        result.put(meshMs, ts);
                    }

                    ts.add(m);
                }
            }

            if (nextMeshMs > 0) {
                calculate(startMs, startMsAlign, currentMs, nextMeshMs, result);
                calculate(endMsAlign, endMs, currentMs, nextMeshMs, result);
            }
        } else {
            if (nextMeshMs > 0) {
                calculate(startMs, endMs, currentMs, nextMeshMs, result);
            }
        }

    }

    private static String format(long ts) {
        return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date(ts));
    }

    private static int nextMesh(int meshMs) {
        switch (meshMs) {
            case 1800000:
                return 300000;
            case 300000:
                return 60000;
            case 60000:
                return 5000;
            default:
                return -1;
        }
    }
}
