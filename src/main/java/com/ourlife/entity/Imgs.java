package com.ourlife.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Imgs {

    @Id
    @Column(name = "img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ourlife_id")
    private OurLife ourLife;

    private String imgUrl;

    //List<MultiPartFile> 인데... 여기도 리스트로 만들어야 되는거 아닐까
    //private List<String> imgUrl = new ArrayList<>();

    public static Imgs createImgs(OurLife ourLife, List<String> fileName){
        Imgs imgs = new Imgs();
        imgs.ourLife = ourLife;
        imgs.imgUrl = listConverter(fileName);
        return imgs;
    }

    public static Imgs createImgs1(List<String> fileName){
        Imgs imgs = new Imgs();
        imgs.imgUrl = listConverter(fileName);
        return imgs;
    }

    public static Imgs updateImgs(Imgs imgs, List<String> fileName){
        imgs.imgUrl = listConverter(fileName);
        return imgs;
    }


    private static String listConverter(List<String> fileName){
        String result = "";
        for (String item: fileName) {
            if(fileName.size() == 1) return result += item;
            result += item + ",";
        }
        return result;
    }
}
