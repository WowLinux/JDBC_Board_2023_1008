package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
 private   int articleLastId = 0;
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
            int id = ++articleLastId;

            PreparedStatement pstmt=null;

            try {

                String sql = "INSERT INTO article";
                sql += " SET regDate =NOW()";
                sql += ", updateDate = NOW()";
                sql += ", title =\"" + title + "\"";
                sql += ", `body` =\"" + body + "\"";

                pstmt = conn.prepareStatement(sql);
                int affectedRows = pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("에러 : " + e );
            }
            finally {
                try {
                    if (pstmt != null && !pstmt.isClosed()) {
                        pstmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.printf("%d번 게시물이 등록 되었습니다.\n", id);
        }
        else if ( rq.getUrlPath().equals("/user/article/list")) {

            PreparedStatement pstmt=null;
            ResultSet rs = null;
            List<Article> articles = new ArrayList<>();
            try {
                String sql = "SELECT * ";
                sql += " FROM article";
                sql += " ORDER BY id DESC";

                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    int id = rs.getInt( "id");
                    String regDate = rs.getString("regDate");
                    String updateDate = rs.getString("updateDate");
                    String title = rs.getString("title");
                    String body = rs.getString("body");
                    Article article = new Article(id,regDate,updateDate,title,body);
                    articles.add(article);

                }

            }
            catch (SQLException e) {
                System.out.println("에러 : " + e );
            }

            finally {
                try {
                    if (rs != null && !rs.isClosed()) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (pstmt != null && !pstmt.isClosed()) {
                        pstmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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


            PreparedStatement pstmt=null;
            try {
                String sql = "UPDATE article";
                sql += " SET regDate =NOW()";
                sql += ", updateDate = NOW()";
                sql += ", title =\"" + title + "\"";
                sql += ", `body` =\"" + body + "\"";
                sql += " WHERE id = " +  id;

                pstmt = conn.prepareStatement(sql);
                int affectedRows = pstmt.executeUpdate();

            }catch (SQLException e) {
                System.out.println("에러 : " + e );
            }
            finally {
                try {
                    if (pstmt != null && !pstmt.isClosed()) {
                        pstmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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

class Article {
    public int id;
    public String regDate;
    public String  updateDate;
    public String title;
    public String body;

    public Article(int id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }
    public Article(int id, String regDate, String updateDate, String title, String body) {
        this.id = id;
        this.regDate = regDate;
        this.updateDate = updateDate;
        this.title = title;
        this.body = body;
    }
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}