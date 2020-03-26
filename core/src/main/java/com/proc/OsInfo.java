package com.proc;

/**
 * @author xiaoyiluo
 * @createTime 2018/11/14 17:03
 **/
public final class OsInfo {
    private static OsInfo instance = null;

    public static OsInfo instance() {
        if (instance == null) {
            synchronized (OsInfo.class) {
                if (instance == null) {
                    instance = new OsInfo();
                }
            }
        }

        return instance;
    }

    private OsInfo() {
        this.name = System.getProperty("os.name");
        this.arch = System.getProperty("os.arch");
        this.fullVersion = System.getProperty("os.version");

        if (this.fullVersion != null && this.fullVersion.length() > 0 && isLinux()) {
            String t1[] = this.fullVersion.split("-");
            if (t1.length == 2) {
                String t2[] = t1[0].split("\\.");
                if (t2.length == 3) {
                    majorVersion = Integer.parseInt(t2[0]);
                    minorVersion = Integer.parseInt(t2[1]);
                    patchVersion = Integer.parseInt(t2[2]);
                }
            }
        }
    }

    // Linux
    private String name;

    // 如 amd64
    private String arch;

    // 3.10.0-693.el7.x86_64
    private int majorVersion;  // 3
    private int minorVersion;  // 10
    private int patchVersion;   // 0
    private String fullVersion;  // 3.10.0-693.el7.x86_64

    @Override
    public String toString() {
        return "OsInfo{" +
                "name='" + name + '\'' +
                ", arch='" + arch + '\'' +
                ", majorVersion=" + majorVersion +
                ", minorVersion=" + minorVersion +
                ", patchVersion=" + patchVersion +
                ", fullVersion='" + fullVersion + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getArch() {
        return arch;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getPatchVersion() {
        return patchVersion;
    }

    public String getFullVersion() {
        return fullVersion;
    }

    public boolean isLinux() {
        return LINUX.equals(this.name);
    }

    public static final String LINUX = "Linux";
}
