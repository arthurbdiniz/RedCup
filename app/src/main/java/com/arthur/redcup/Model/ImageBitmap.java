package com.arthur.redcup.Model;


import java.io.Serializable;
import java.util.HashMap;


public class ImageBitmap implements Serializable {



    HashMap<Integer, String> paths = new HashMap<Integer, String>();

    public ImageBitmap(){

    }

    public void addPath(Integer id, String path){
        paths.put(id, path);
    }

    public String getPath(Integer id){
        return paths.get(id);
    }

    public void removePath(Integer id){
        paths.remove(id);
    }

    public void size(){
        paths.size();
    }


}
