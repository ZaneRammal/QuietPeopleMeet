package ca.uoit.quietpeoplemeet1;

import java.io.Serializable;

public class TextNode implements Serializable {

    public String message;


    public TextNode(String mes){
        this.message = mes;

    }

    public String getMessageLevel() {
        return message;
    }




}
