package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    public void run() {
        Scanner sc = Container.scanner;
       List<Article> articles = new ArrayList<>();

        int articleLastId = 0;
        while (true) {
            System.out.printf("명령어) " );
            String  cmd = sc.nextLine();

            if ( cmd.equals("/user/article/write")) {
                System.out.println("== 게시물 등록 == ");
                System.out.printf("제목 :  ");
                String title = sc.nextLine();
                System.out.printf("내용 :  ");
                String body = sc.nextLine();
                int id= ++articleLastId;

                Article article = new Article(id,title,body);
                articles.add(article);

                System.out.println("생성된 객체물 :    " + article);
                System.out.printf("%d번 게시물이 등록 되었습니다.\n", article.id);
            }
            else if ( cmd.equals("/user/article/list")) {
                System.out.println("== 게시물 리스트 == ");

                if (articles.isEmpty()) {
                    System.out.println("게시물이 존재하지 않습니다.");
                    continue;
                }

                System.out.println("번호  /  제목");

                for(Article article : articles) {
                    System.out.printf("%d / %s\n" , article.id, article.title);

                }
            }
            else if (cmd.equals("system exit")) {
                System.out.println("시스템 종료");
                break;
            }
            else {
                System.out.println("명령어를 확인 해주세요");
            }
        }
        sc.close();
    }
}

class Article {
    int id;
    String title;
    String body;

    public Article(int id, String title, String body) {
        this.id = id;
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