package com.sbytestream;

public class QueryWork {
    public QueryWork(String query, String file) {
        setQuery(query);
        setFilename(file);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String toString() {
        return String.format("File: %s, query: %s", filename, query);
    }

    private String query;
    private String filename;
}
