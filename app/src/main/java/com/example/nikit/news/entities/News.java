package com.example.nikit.news.entities;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nikit on 12.03.2017.
 */

public class News {
    private String status;
    private String source;
    private String sortBy;
    private ArrayList<Article> articles;

    public News() {
        articles = new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "News{" +
                "status='" + status + '\'' +
                ", source='" + source + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", articles=" + articles +
                '}';
    }

    public int getArticlesCount() {
        return articles.size();
    }

    public static class Article {
        private String articleId;
        private String author;
        private String title;
        private String description;
        private String url;
        private String urlToImage;
        private String publishedAt;
        private CharSequence source;
        private boolean liked = false;

        public String getArticleId() {
            if (articleId != null) {
                return articleId;
            } else {
                articleId = url.replaceAll("[\\.#\\$\\[\\]/]", "");
            }
            return articleId;
        }

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public CharSequence getSource() {
            return source;
        }

        public void setSource(CharSequence source) {
            this.source = source;
        }

        public void setSourceId(String sourceId) {
            this.source = sourceId;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public void setUrlToImage(String urlToImage) {
            this.urlToImage = urlToImage;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        @Override
        public String toString() {
            return "Article{" +
                    "author='" + author + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", url='" + url + '\'' +
                    ", urlToImage='" + urlToImage + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    '}';
        }

        public HashMap<String, Object> toMap() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("title", title);
            hashMap.put("description", description);
            hashMap.put("url", url);
            hashMap.put("urlToImage", urlToImage);
            hashMap.put("publishedAt", publishedAt);
            hashMap.put("author", author);

            return hashMap;
        }

    }


}
