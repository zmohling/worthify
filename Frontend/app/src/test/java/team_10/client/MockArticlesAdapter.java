package team_10.client;

import android.support.v4.app.FragmentManager;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import team_10.client.object.Article;
import team_10.client.utility.ArticlesAdapter;

import static junit.framework.TestCase.assertEquals;

public class MockArticlesAdapter {
    @Mock
    FragmentManager mockFragmentManager;
    @Test
    public void testArticleAmountInAdapter() {
        Article test = new Article("title", "description", "pictureUrl", "url");
        ArrayList<Article> testArticles = new ArrayList<Article>();
        testArticles.add(test);
        ArticlesAdapter testAdapter = new ArticlesAdapter(testArticles, mockFragmentManager);
        assertEquals(1, testAdapter.getItemCount());
    }
}
