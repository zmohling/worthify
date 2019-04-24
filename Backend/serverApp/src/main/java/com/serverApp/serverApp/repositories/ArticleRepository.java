package com.serverApp.serverApp.repositories;

import com.serverApp.serverApp.models.Vote;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContexts;
import java.util.List;

/**
 * @author Griffin Stout
 *
 * Repository to make custom queries on the Article Table
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * custom query to count articles with param id
     * @param id to search for
     * @return the number of articles with param id
     */
    @Query(value = "SELECT COUNT(id) FROM articles WHERE id = ?1", nativeQuery = true)
    int getNumArticles(long id);

    /**
     * custom query to return all of the articles in the DB
     * @return list of all of the articles
     */
    @Query(value = "SELECT * FROM articles", nativeQuery = true)
    Article[] getAllArticles();

    /**
     * custom query to check for duplicated
     * @param title title to check duplicated of
     * @return the number of duplicates
     */
    @Query(value = "SELECT count(*) FROM articles WHERE url = ?1", nativeQuery= true)
    int getDuplicates(String title);

    /**
     * custom query to return all articles with a certain keyword
     * @param keyword keyword to check for
     * @return list of articles with keyword
     */
    @Query(value = "SELECT * FROM articles WHERE keyword = ?1 AND is_active = 1", nativeQuery = true)
    Article[] getAllArticlesWithKeyword(String keyword);

    /**
     * custom query to archive an article
     * @param id of the article to donate
     * @return id of the article deleted
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE articles SET is_active = 0 WHERE id= ?1", nativeQuery = true)
    int deleteArticle(int id);

}
