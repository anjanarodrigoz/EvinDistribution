package com.example.evindistribution.Models;

public class Search {

    String id,search;

    public Search(String id, String search) {
        this.id = id;
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
