package com.example.asus.androidcomicreader.Model;

public class Comic {

    private int ID;
    private String Name;
    private String Image;

    public Comic() {
    }

    public Comic(int ID, String name, String image) {
        this.ID = ID;
        Name = name;
        Image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
