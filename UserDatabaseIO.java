package pers.euphoria.aircraftbattle;

import java.sql.*;

/**
 * JDBC连接数据库
 * 1. 连接前准备
 * - 数据库：新建数据库ShootGame
 * - 表：新建表score
 * - 加入字段：id, user_name, score
 *
 * @author Wang Qinhong
 */

class UserDatabaseIO {
    private static final String URL = "jdbc:mysql://localhost:3306/" +
            "shootgame?useUnicode=true&characterEncoding=utf-8"
            + "&serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true";

    private static final String USER = "root";

    private static final String PASSWORD = "610106";

    // 1. 加载驱动路径
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // 2. 定义连接对象
    private static Connection connection;

    // 3. 定义传输器对象
    private static Statement statement;

    /**
     * 获取传输器
     */
    private static void databaseConnection() {
        // 1. 注册数据库驱动
        try {
            // 加载数据库驱动
            Class.forName(DRIVER);
            // 获取数据库连接
            connection = DriverManager.getConnection(URL, USER, PASSWORD); // DriverManager: 驱动管理类
            if (connection != null) {
                // 获取传输器
                statement = connection.createStatement();
            } else {
                throw new RuntimeException();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据插入操作
     *
     * @param userName 用户名
     * @param score    得分
     */
    static void insert(String userName, int score) {
        UserDatabaseIO.databaseConnection();
        String sql = String.format("insert into score(user_name, score) values('%s', %d);", userName, score);
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询前十名
     *
     * @return 查询集
     */
    static String[] select() {
        String sql = String.format("select * from score order by score.score desc limit %d", GameFactory.MAX_HIGH_SCORE);
        ResultSet resultSet = null; // 利用查询结果集保存查询值
        String[] results = new String[GameFactory.MAX_HIGH_SCORE];

        UserDatabaseIO.databaseConnection(); // 获取传输器
        try {
            resultSet = statement.executeQuery(sql);
            for (int i = 0; i < GameFactory.MAX_HIGH_SCORE; i++) {
                if (resultSet.next()) {
                    results[i] = String.format("No.%-8d%-16s%-10d", i + 1, resultSet.getString("user_name"), resultSet.getInt("score"));
                } else {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert resultSet != null;
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
