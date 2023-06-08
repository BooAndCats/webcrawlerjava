import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;


public class WebCrawler
{
    private Set<String> visitedUrls;
    private List<String> urlsToVisit;
    private File file;

    public WebCrawler()
    {
        visitedUrls = new HashSet<>();
        urlsToVisit = new LinkedList<>();
        file = new File("results.txt");
    }

    public void crawl(String seedUrl, int maxPages, String word)
    {
        urlsToVisit.add(seedUrl);

        while (!urlsToVisit.isEmpty() && visitedUrls.size() < maxPages)
        {
            String url = urlsToVisit.remove(0);
            // && url.contains is the site limiter to the site
            if (!visitedUrls.contains(url) /*&& url.contains("website to stay on")*/)
            {
                System.out.println("Crawling: " + url);
                visitedUrls.add(url);
                try
                {
                    Document document = Jsoup.connect(url).get();
                    Elements links = document.select("a[href]");

                    int count = countOccurrences(document.text(), word);

                    for (Element link : links)
                    {
                        String linkedUrl = link.absUrl("href");
                        if (!visitedUrls.contains(linkedUrl))
                            urlsToVisit.add(linkedUrl);
                    }

                    System.out.println(count + " occurrences of " + word);

                    if (count != 0)
                    {
                        try
                        {
                            FileWriter printwriter = new FileWriter(file, true);
                            printwriter.write(url + " has " + count + " occurrences of " + word + "\n");
                            printwriter.close();
                        }
                        catch (Exception e)
                        {
                            System.out.println(e);
                        }
                    }
                }
                catch (IOException ex)
                {
                    System.out.println("Error crawling: " + url + " since " + ex);
                }
                catch (Exception e)
                {
                    System.out.println("Exception: " + e);
                }
            }
        }
        System.out.println("Crawling finished. Visited " +visitedUrls.size()+ " unique pages.");
    }

    private int countOccurrences(String text, String word)
    {
        String[] words = text.split("\\W+");
        int count = 0;

        for (String w : words)
        {
            if (w.equalsIgnoreCase(word))
                count++;
        }
        return count;
    }
    public static void main(String[] args)
    {
        WebCrawler webCrawler = new WebCrawler();
        webCrawler.crawl("website to start", maximum crawls, "word to find");
    }
}

