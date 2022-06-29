package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element row = document.selectFirst(".style-ugc");
        return row.text();
    }

    private Post completion(Element row) {
        Element titleElement = row.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        String vacancyName = titleElement.text();
        String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));

        Element titleDateElement = row.select(".vacancy-card__date").first();
        Element linkDateElement = titleDateElement.child(0);
        String date = linkDateElement.attr("datetime");
        String description = null;
        try {
            description = retrieveDescription(vacancyLink);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Post(vacancyName, vacancyLink, description, new HabrCareerDateTimeParser().parse(date));
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> list = new ArrayList();
        for (int i = 1; i < 6; i++) {
            Connection connection = Jsoup.connect(PAGE_LINK + i);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                list.add(completion(row));
            });
        }
        return list;
    }
}
