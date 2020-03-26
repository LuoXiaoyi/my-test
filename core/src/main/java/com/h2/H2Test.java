package com.h2;

import java.sql.*;

/**
 * @author xiluo
 * @ClassName
 * @Description TODO
 * @Date 2020/3/4 14:33
 * @Version 1.0
 **/
public class H2Test {
    static final String URL = "jdbc:h2:file:./h2test;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
    static final String USER = "sa";
    static final String PASS_WD = "";
    static final String DRIVER = "org.h2.Driver";

    static final String CREATE_TEST_TABLE_SQL = "CREATE CACHED TABLE IF NOT EXISTS TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))";
    static final String DROP_TEST_TABLE_SQL = "DROP TABLE IF EXISTS TEST";

    public static void main(String[] args) throws Exception {
        Class.forName(DRIVER);
        Connection conn = DriverManager.getConnection(URL, USER, PASS_WD);
        System.out.println(conn);

        Statement ps = conn.createStatement();
        ps.execute(DROP_TEST_TABLE_SQL);
        ps.execute(CREATE_TEST_TABLE_SQL);
        ps.execute("insert into TEST values(1, 'xiluo')");
        ResultSet rs = ps.executeQuery("select * from TEST where id = 2");
        while (rs != null && rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("name");
            System.out.println("id: " + id + ", name: " + name);
        }
    }
}
