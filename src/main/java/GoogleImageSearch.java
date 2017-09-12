package main.java;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GoogleImageSearch {

    // Default parameters
    private String url = "https://www.googleapis.com/customsearch/v1";
    private String key = "AIzaSyB0xYU-WgdfGh7vU0jEp6lfTvM-hDyTkHg";
    private String cx = "abiding-center-171519";
    private String searchType = "image";

    // Custom parameters
    private String searchQuery = null;
    private String siteSearch = null;
    private String tbs = null;

    // Constructor
    public GoogleImageSearch(String searchQuery, String siteSearch, int imageWidth, int imageHeight) throws UnsupportedEncodingException {
        this.searchQuery = URLEncoder.encode(searchQuery, "UTF-8");
        this.siteSearch  = siteSearch;
        this.tbs = "tbs=isz:ex,iszw:" + imageWidth + ",iszh:" + imageHeight;
    }
    // Build Google Image Custom Search query
    public String buildImgeSearchQuery() {
        return               this.url + "?" + 
            "key=" +         this.key + "&" +
            "cx=" +          this.cx + "&" +
            "searchType=" +  this.searchType + "&" +
            "q=" +           this.searchQuery + "&" +
            "siteSearch=" +  this.siteSearch + "&" +
            "tbs=" +         this.tbs;
    }

}