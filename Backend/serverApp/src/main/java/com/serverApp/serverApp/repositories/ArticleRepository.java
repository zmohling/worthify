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


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {


    @Query(value = "SELECT COUNT(id) FROM articles WHERE id = ?1", nativeQuery = true)
    int getNumArticles(long id);

    @Query(value = "SELECT * FROM articles", nativeQuery = true)
    Article[] getAllArticles();

    @Query(value = "SELECT count(*) FROM articles WHERE url = ?1", nativeQuery= true)
    int getDuplicates(String title);

    @Query(value = "SELECT * FROM articles WHERE keyword = ?1 AND is_active = 1", nativeQuery = true)
    Article[] getAllArticlesWithKeyword(String keyword);

    @Modifying
    @Transactional
    @Query(value = "UPDATE articles SET is_active = 0 WHERE id= ?1", nativeQuery = true)
    int deleteArticle(int id);

  @Modifying
  @Transactional
  @Query(
      value =
          "UPDATE networthr.articles SET description = REPLACE(description, 'â€œ', '“');\n"
              + "UPDATE networthr.articles SET description = REPLACE(description, 'â€�', '”');\n"
              + "UPDATE networthr.articles SET description = REPLACE(description, 'â€™', '’');\n"
              + "UPDATE networthr.articles SET description = REPLACE(description, 'â€˜', '‘');\n"
              + "UPDATE networthr.articles SET description = REPLACE(description, 'â€”', '–');\n"
              + "UPDATE networthr.articles SET description = REPLACE(description, 'â€“', '—');\n"
              + "UPDATE networthr.articles SET description = REPLACE(description, 'â€¢', '-');\n"
              + "UPDATE networthr.articles SET description = REPLACE(description, 'â€¦', '…');\n"
              + "\n"
              + "UPDATE networthr.articles SET title = REPLACE(title, 'â€œ', '“');\n"
              + "UPDATE networthr.articles SET title = REPLACE(title, 'â€�', '”');\n"
              + "UPDATE networthr.articles SET title = REPLACE(title, 'â€™', '’');\n"
              + "UPDATE networthr.articles SET title = REPLACE(title, 'â€˜', '‘');\n"
              + "UPDATE networthr.articles SET title = REPLACE(title, 'â€”', '–');\n"
              + "UPDATE networthr.articles SET title = REPLACE(title, 'â€“', '—');\n"
              + "UPDATE networthr.articles SET title = REPLACE(title, 'â€¢', '-');\n"
              + "UPDATE networthr.articles SET title = REPLACE(title, 'â€¦', '…');", nativeQuery = true)
  void fixArticles();

  @Modifying
  @Transactional
  @Query(value = "UPDATE articles SET voters = ?1 WHERE id = ?2", nativeQuery = true)
  int updateArticleVoters(List<Vote> newVoters, long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE articles SET votes = ?1 WHERE id = ?2", nativeQuery = true)
    int updateArticleVotes(int votes, long id);

}
