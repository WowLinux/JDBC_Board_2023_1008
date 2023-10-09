package org.example;

import org.example.Util.DBUtil;
import org.example.Util.SecSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    public void run() {
        Scanner sc = Container.scanner;
       while (true) {
            System.out.printf("명령어) " );
            String  cmd = sc.nextLine();
            Rq rq = new Rq(cmd);
            Connection conn = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e){
                System.out.println("예외  : MySQL 드라이버 로딩 실패");
                System.out.println("프로그램을 종료합니다.");
                break;
            }
            String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
            try {
                conn = DriverManager.getConnection(url, "root", "1234");

                int actionResult = doAction(conn, sc, rq, cmd);
                if (actionResult == -1) {
                     break;
                }

            } catch (SQLException e) {
                System.out.println("예외  : MySQL 드라이버 로딩 실패");
                System.out.println("프로그램을 종료합니다.");
                 break;
            }

            finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        sc.close();
    }

    private int  doAction(Connection conn, Scanner sc, Rq rq, String cmd) {

        if ( rq.getUrlPath().equals("/user/article/write")) {
            System.out.println("== 게시물 등록 == ");
            System.out.printf("제목 :  ");
            String title = sc.nextLine();
            System.out.printf("내용 :  ");
            String body = sc.nextLine();

            SecSql  sql = new SecSql();
            sql.append("INSERT INTO article");
            sql.append(" SET regDate = NOW()");
            sql.append(", updateDate = NOW()");
            sql.append(", title = ?", title);
            sql.append(", `body` = ?", body);
            int id = DBUtil.insert(conn, sql);

            System.out.printf("%d번 게시물이 등록 되었습니다.\n", id);
        }
        else if ( rq.getUrlPath().equals("/user/article/list")) {

            List<Article> articles = new ArrayList<>();

            SecSql sql = new SecSql();
            sql.append("SELECT *");
            sql.append("FROM article");
            sql.append("ORDER BY id DESC");
            List<Map<String, Object>> articleListMap =  DBUtil.selectRows(conn, sql);
            for (Map<String, Object> articleMap : articleListMap) {
                articles.add(new Article(articleMap));
            }
            System.out.println("== 게시물 리스트 == ");

            if (articles.isEmpty()) {
                System.out.println("게시물이 존재하지 않습니다.");
                return -1;
            }

            System.out.println("번호  /  제목");

            for(Article article : articles) {
                System.out.printf("%d / %s\n" , article.id, article.title);

            }
        }
        else if ( rq.getUrlPath().equals("/user/article/modify")) {

            int id = rq.getIntParam("id", 0);
            if (id == 0) {
                System.out.println("id를 올바르게 입력해주세요.");
                return -1;
            }
            System.out.println("== 게시물 수정 == ");
            System.out.printf("새제목 :  ");
            String title = sc.nextLine();
            System.out.printf("새내용 :  ");
            String body = sc.nextLine();

            SecSql  sql = new SecSql();
            sql.append("UPDATE  article");
            sql.append(" SET updateDate = NOW()");
            sql.append(", title = ?", title);
            sql.append(", `body` = ?", body);
            sql.append("WHERE  id = ?", id);
           DBUtil.update(conn, sql);

            System.out.printf("%d번 게시물이 수정 되었습니다.\n", id);
        }
        else if (cmd.equals("system exit")) {
            System.out.println("시스템 종료");
            System.exit(0);
        }
        else {
            System.out.println("명령어를 확인 해주세요");
        }
        return 0;
    }
}

