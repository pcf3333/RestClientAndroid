package service;


import java.util.*;

import util.RandomUtils;

public class Track {

    String title;
    String singer;
    String id;

    public Track(String title, String singer){
        this.title=title;
        this.singer=singer;
        this.id=RandomUtils.getId();
    }

    @Override
    public String toString() {
        return "Song -> "+title + " from ->" + singer ;
    }

    public String getTitle() {
        return title;
    }

    public String getSinger() {
        return singer;
    }

    public String getId() {
        return id;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = RandomUtils.getId();
    }
}
