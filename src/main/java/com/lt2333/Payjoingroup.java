package com.lt2333;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.sql.*;

public final class Payjoingroup extends JavaPlugin {
    public static final Payjoingroup INSTANCE = new Payjoingroup();

    //JDBC_DRIVER
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    //数据库连接信息
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/fk";
    static final String USER = "fk";
    static final String PASS = "password";

    //拒绝申请理由
    static final String REJECT_STRING = "请在xxx.xxx.com支付后入群";

    private Payjoingroup() {
        super(new JvmPluginDescriptionBuilder("com.lt2333.payjoingroup", "1.0")
                .name("QQ付费入群系统")
                .author("LittleTurtle2333")
                .build());
    }

    @Override
    public void onEnable() {

        //群入群申请监听器
        Listener listener2 = GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinRequestEvent.class, event -> {
            long qqid = event.getFromId();

            try {
                if (getGroupWhiteList(qqid)) {
                    event.accept();
                } else {
                    event.reject(false, REJECT_STRING);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });
        listener2.start();

        //成员新增监听器
        Listener listener3 = GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.class, event -> {

            MessageChain chain = new MessageChainBuilder()
                    .append(new At(event.getUser().getId()))
                    .append("\n" +
                            "欢迎加入：" + event.getGroup().getName() + "\n")
                    .append("Tips:你已成功进群,已执行删除相关订单数据,退群不作退费,并无法再次申请")
                    .build();

            event.getGroup().sendMessage(chain);

            try {
                updateGroupWhiteList(event.getUser().getId());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        });
        listener3.start();

        getLogger().info("PayJoinGroup Plugin loaded!");
    }

    //获取入群白名单
    public boolean getGroupWhiteList(long qqid) throws SQLException, ClassNotFoundException {

        Connection conn = null;
        Statement stmt = null;
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // 执行查询
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT * FROM `orders` WHERE `shopid` = 1 AND `qq` = " + qqid + " AND `state` = 1";
        ResultSet rs = stmt.executeQuery(sql);

        boolean bl = false;
        // 展开结果集数据库
        while (rs.next()) {
            // 通过字段检索
            int state = rs.getInt("state");

            if (state == 1) {
                bl = true;
                break;
            }
        }
        // 完成后关闭
        rs.close();
        stmt.close();
        conn.close();
        return bl;
    }

    //更新入群白名单
    public void updateGroupWhiteList(long qqid) throws SQLException, ClassNotFoundException {

        Connection conn = null;
        Statement stmt = null;
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // 执行查询

        stmt = conn.createStatement();
        String sql;
        sql = "SELECT * FROM `orders` WHERE `shopid` = 1 AND `qq` = " + qqid + " AND `state` = 1";
        ResultSet rs = stmt.executeQuery(sql);


        // 展开结果集数据库
        while (rs.next()) {
            // 通过字段检索
            int state = rs.getInt("state");
            int id = rs.getInt("id");


            if (state == 1) {
                String sql2;
                sql2 = "UPDATE `orders` SET `state` = 2 WHERE `orders`.`id` = " + id;
                stmt.executeUpdate(sql2);
                break;
            }
        }

        // 完成后关闭
        rs.close();
        stmt.close();
        conn.close();
    }

}