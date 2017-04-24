package com.example.nikit.news.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nikit.news.entities.News;
import com.example.nikit.news.entities.Source;
import com.example.nikit.news.database.DataBaseContract.SourceTable;
import com.example.nikit.news.database.DataBaseContract.NewsTable;
import com.example.nikit.news.database.DataBaseContract.LikedNewsTable;

import com.example.nikit.news.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nikit on 13.01.2017.
 */

public class SqLiteDbHelper extends SQLiteOpenHelper {

    public SqLiteDbHelper(Context context) {
        super(context, DataBaseContract.DB_NAME, null, DataBaseContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseContract.SourceTable.CREATE_TABLE);
        db.execSQL(DataBaseContract.NewsTable.CREATE_TABLE);
        db.execSQL(LikedNewsTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            db.execSQL(DataBaseContract.SourceTable.DELETE_TABLE);
            db.execSQL(DataBaseContract.SourceTable.CREATE_TABLE);

            db.execSQL(DataBaseContract.NewsTable.DELETE_TABLE);
            db.execSQL(DataBaseContract.NewsTable.CREATE_TABLE);

            db.execSQL(LikedNewsTable.DELETE_TABLE);
            db.execSQL(LikedNewsTable.CREATE_TABLE);

        }
    }

    private ContentValues getContentValuesFormSource(Source source) {
        ContentValues values = new ContentValues();
        values.put(SourceTable._ID, source.getId());
        values.put(SourceTable.COLUMN_NAME_NAME, source.getName());
        values.put(SourceTable.COLUMN_NAME_DESCRIPTION, source.getDescription());
        values.put(SourceTable.COLUMN_NAME_LANGUAGE, source.getLanguage());
        values.put(SourceTable.COLUMN_NAME_COUNTRY, source.getCountry());
        values.put(SourceTable.COLUMN_NAME_CATEGORY, source.getCategory());
        values.put(SourceTable.COLUMN_NAME_SORT_BY_AVAILABLE, Util.getStringFromArrayOfString(source.getSortBysAvailable()));
        values.put(SourceTable.COLUMN_NAME_URL, source.getUrl());
        values.put(SourceTable.COLUMN_NAME_URL_LOGOS_SMALL, source.getUrlsToLogos().getSmall());
        values.put(SourceTable.COLUMN_NAME_URL_LOGOS_MEDIUM, source.getUrlsToLogos().getMedium());
        values.put(SourceTable.COLUMN_NAME_URL_LOGOS_LARGE, source.getUrlsToLogos().getLarge());
        return values;

    }

    private ContentValues getContentValuesFromArticle(News.Article article) {
        ContentValues values = new ContentValues();
        values.put(NewsTable._ID, article.getArticleId());
        values.put(NewsTable.COLUMN_NAME_AUTHOR, article.getAuthor());
        values.put(NewsTable.COLUMN_NAME_DESCRIPTION, article.getDescription());
        values.put(NewsTable.COLUMN_NAME_PUBLISHED_AT, article.getPublishedAt());
        values.put(NewsTable.COLUMN_NAME_TITLE, article.getTitle());
        values.put(NewsTable.COLUMN_NAME_SOURCE_ID, article.getSource().toString());
        values.put(NewsTable.COLUMN_NAME_URL, article.getUrl());
        values.put(NewsTable.COLUMN_NAME_URL_IMAGE, article.getUrlToImage());
        return values;
    }

    public void insertArticle(SQLiteDatabase db, News.Article article) {
        db.insert(NewsTable.TABLE_NAME, null, getContentValuesFromArticle(article));
    }

    public void insertArticles(SQLiteDatabase db, List<News.Article> articles) {
        for (News.Article article : articles) {
            db.insert(NewsTable.TABLE_NAME, null, getContentValuesFromArticle(article));
        }
    }

    public void insertSource(SQLiteDatabase db, Source source) {
        db.insert(SourceTable.TABLE_NAME, null, getContentValuesFormSource(source));
    }

    public void insertSource(SQLiteDatabase db, List<Source> sourceList) {
        for (Source source : sourceList) {
            getContentValuesFormSource(source);
            db.insert(SourceTable.TABLE_NAME, null, getContentValuesFormSource(source));
        }
    }

    private News.Article getArticleFromCursor(Cursor cursor) {
        News.Article article = new News.Article();
        article.setAuthor(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_NAME_AUTHOR)));
        article.setTitle(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_NAME_TITLE)));
        article.setDescription(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_NAME_DESCRIPTION)));
        article.setUrl(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_NAME_URL)));
        article.setUrlToImage(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_NAME_URL_IMAGE)));
        article.setPublishedAt(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_NAME_PUBLISHED_AT)));
        article.setSourceId(cursor.getString(cursor.getColumnIndex(NewsTable.COLUMN_NAME_SOURCE_ID)));

        return article;
    }

    public ArrayList<News.Article> getAllArticles(SQLiteDatabase db) {
        ArrayList<News.Article> articles = new ArrayList<>();
        Cursor cursor = db.query(DataBaseContract.NewsTable.TABLE_NAME,
                DataBaseContract.NewsTable.ARRAY_OF_COLUMN_NAMES,
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                News.Article article = getArticleFromCursor(cursor);
                article.setLiked(isLikedNewsContain(db, article.getArticleId()));
                articles.add(article);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return articles;
    }



    private Source getSourceFromCursor(Cursor cursor) {
        Source source = new Source();

        source.setId(cursor.getString(cursor.getColumnIndex(SourceTable._ID)));
        source.setName(cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_NAME)));
        source.setDescription(cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_DESCRIPTION)));
        source.setCategory(cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_CATEGORY)));
        source.setCountry(cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_COUNTRY)));
        source.setLanguage(cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_LANGUAGE)));
        source.setUrl(cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_URL)));

        Source.UrlToLogos urlToLogos = new Source.UrlToLogos();
        urlToLogos.setSmall(cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_URL_LOGOS_SMALL)));
        urlToLogos.setMedium(cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_URL_LOGOS_MEDIUM)));
        urlToLogos.setLarge(cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_URL_LOGOS_LARGE)));
        source.setUrlsToLogos(urlToLogos);

        String[] sortByAvail = Util.getListOfStringsFromString(cursor.getString(
                cursor.getColumnIndex(SourceTable.COLUMN_NAME_SORT_BY_AVAILABLE)));

        source.setSortBysAvailable(sortByAvail);

        return source;
    }

    public HashMap<Integer, String> getSourcesId(SQLiteDatabase db) {
        HashMap<Integer, String> result = new HashMap<>();
        Cursor cursor = db.query(SourceTable.TABLE_NAME,
                new String[]{SourceTable._ID, SourceTable.COLUMN_NAME_NAME},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(SourceTable._ID));
                String name = cursor.getString(cursor.getColumnIndex(SourceTable.COLUMN_NAME_NAME));
                result.put(id, name);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public void getAllSources(SQLiteDatabase db, List<Source> sourceList) {
        Cursor cursor = db.query(DataBaseContract.SourceTable.TABLE_NAME,
                DataBaseContract.SourceTable.ARRAY_OF_COLUMN_NAMES,
                null, null, null, null, null);

        if (cursor.moveToFirst()) {

            do {
                Source source = getSourceFromCursor(cursor);
                sourceList.add(source);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }


    public boolean isLikedNewsContain(SQLiteDatabase db, String newsId) {
        Cursor cursor = db.query(LikedNewsTable.TABLE_NAME,
                new String[]{LikedNewsTable._ID},
                LikedNewsTable._ID + "=?",
                new String[]{newsId},
                null, null, null);

        if (cursor.moveToFirst()) {
            if (cursor.getString(0).equals(newsId)) {
                return true;
            }
        }
        return false;
    }

    public void addLikedNews(SQLiteDatabase db, String newsId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LikedNewsTable._ID, newsId);
        db.insert(LikedNewsTable.TABLE_NAME, null, contentValues);
    }

    public void removeLikedNews(SQLiteDatabase db, String newsId) {
        db.delete(LikedNewsTable.TABLE_NAME,
                LikedNewsTable._ID + "=?",
                new String[]{newsId});
    }

    public void addAllLikedNewses(SQLiteDatabase db, HashMap<String, String> newsIds) {

        ContentValues contentValues;
        for (String item : newsIds.keySet()) {
            contentValues = new ContentValues();
            contentValues.put(LikedNewsTable._ID, item);
            db.insert(LikedNewsTable.TABLE_NAME, null, contentValues);
        }
    }

    public void clearSourceTable(SQLiteDatabase db) {
        db.delete(SourceTable.TABLE_NAME, null, null);
    }

    public void clearLikedNewsTable(SQLiteDatabase db) {
        db.delete(LikedNewsTable.TABLE_NAME, null, null);
    }

    public void clearNewsTable(SQLiteDatabase db) {
        db.delete(NewsTable.TABLE_NAME, null, null);
    }
}
