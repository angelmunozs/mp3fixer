package main.java;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GoogleImageSearch {

    // Default parameters
    private String url = "https://www.googleapis.com/customsearch/v1";
    private String key = "AIzaSyAt6P4Ns4MI790gR-c9eELqk0UAhGTqQJI";
    private String cx = "008514602339306092424:k3cqvrjyhum";
    private String searchType = "image";
    private String numResults = "1";
    private String imgSize = "medium";

    // Custom parameters
    private String searchQuery = null;
    private String siteSearch = null;

    // Constructor
    public GoogleImageSearch(String searchQuery, String siteSearch, int imageWidth, int imageHeight) throws UnsupportedEncodingException {
        this.searchQuery = URLEncoder.encode(searchQuery, "UTF-8");
        this.siteSearch  = siteSearch;
    }
    // Build Google Image Custom Search query
    public String buildImgeSearchQuery() {
        return               this.url + "?" + 
            "key=" +         this.key + "&" +
            "cx=" +          this.cx + "&" +
            "searchType=" +  this.searchType + "&" +
            "q=" +           this.searchQuery + "&" +
            "siteSearch=" +  this.siteSearch + "&" +
            "num=" +         this.numResults + "&" +
            "imgSize=" +     this.imgSize;
    }

}