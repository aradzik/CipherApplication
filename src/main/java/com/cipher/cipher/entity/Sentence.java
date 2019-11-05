package com.cipher.cipher.entity;
import javax.persistence.*;

@Entity
@Table(name = "sentence")
public class Sentence {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String text;
    private String code;
    private String encode;

    public Sentence() {
    }

    public Sentence(String text, String code,String encode) {
        this.text = text;
        this.code = code;
        this.encode = encode;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public Integer getId () {
            return id;
        }

        public void setId (Integer id){
            this.id = id;
        }

        public String getText () {
            return text;
        }

        public void setText (String text){
            this.text = text;
        }

        public String getCode () {
            return code;
        }

        public void setCode (String code){
            this.code = code;
        }
    }
