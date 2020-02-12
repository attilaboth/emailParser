package com.tsystems.parser.email.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Vector;

public class HtmlParser {

    private static HtmlParser htmlParser = null;

    private Document document = null;

    private HtmlParser() {
    }

    public static HtmlParser getHtmlParserInstance() {
        if (htmlParser == null) {
            htmlParser = new HtmlParser();
        }
        return htmlParser;
    }

    public void setDocument(final String htmlContentAsStr) {
        this.document = Jsoup.parse(htmlContentAsStr, "utf-8");
    }

    public String getTableBody() {
        return document.body().getElementsByTag("tbody").toString();
    }

    public void extractTableContentToConsole() {

        Elements rows = document.select("tr");

        for (Element row : rows) {
            Elements columns = row.select("td");
            for (Element column : columns) {
                String aRow = column.text() + " | ";
                System.out.println(aRow);
            }
            System.out.println();
        }
    }

    public StringBuilder mapTableContentToStringBuilder(final String htmlContentAsStr) {
        StringBuilder contentReturn = new StringBuilder();
        setDocument(htmlContentAsStr);

        StringBuilder aRowAsStrB = null;

        Elements rows = document.select("tr");

        for (Element row : rows) {
            aRowAsStrB = new StringBuilder();

            Elements columns = row.select("td");
            for (Element column : columns) {
                String aRow = column.text() + "_";
                aRowAsStrB.append(aRow);
            }

            final String aRowData = aRowAsStrB.toString();
            if (!aRowData.startsWith("_") && !Character.isAlphabetic(aRowData.charAt(0))) {
                contentReturn.append(aRowData);
            }
        }
        return contentReturn;
    }


    public List<String> mapTableContentToVector(final String htmlContentAsStr) {
        List<String> contentReturn = new Vector<>();
        setDocument(htmlContentAsStr);

        StringBuilder aRowAsStrB = null;

        Elements rows = document.select("tr");

        for (Element row : rows) {
            aRowAsStrB = new StringBuilder();

            Elements columns = row.select("td");
            for (Element column : columns) {
                String aRow = column.text() + "_";
                aRowAsStrB.append(aRow);
            }

            final String aRowData = aRowAsStrB.toString();
            if (!aRowData.startsWith("_") && !Character.isAlphabetic(aRowData.charAt(0))) {
                contentReturn.add(aRowData);
            }
        }
        return contentReturn;
    }

}
