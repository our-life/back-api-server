package com.ourlife.entity;

import com.ourlife.dto.ourlife.CreateOurlifeRequest;
import com.ourlife.dto.ourlife.UpdateOurlifeRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OurLife extends BaseEntity {

    @Id
    @Column(name = "ourlife_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String contents;

    @OneToMany(cascade = CascadeType.ALL)
    List<Imgs> imgsList = new ArrayList<>();

    //좋아요 연관관계 생각해보기
/*    @OneToMany(cascade = CascadeType.ALL)
    List<OurlifeLike> ourlifeLikes = new ArrayList<>();*/


    public static OurLife createOurlife(CreateOurlifeRequest request, User user, Imgs imgs) {
        OurLife ourLife = new OurLife();
        ourLife.user = user;
        ourLife.contents = request.getContents();
        ourLife.imgsList.add(imgs);
        addImgs(ourLife,imgs);
        return ourLife;
    }

    public static Imgs addImgs(OurLife ourLife, Imgs imgs){
        imgs.setOurLife(ourLife);
        return imgs;
    }

/*
    public static OurLife likeOurlife(OurLife ourLife, OurlifeLike ourlifeLike){
        ourLife.ourlifeLikes.add(ourlifeLike);
        addLikes(ourLife, ourlifeLike);
        return ourLife;
    }
    public static OurlifeLike addLikes(OurLife ourLife, OurlifeLike ourlifeLike){
        ourlifeLike.setOurLife(ourLife);
        return ourlifeLike;
    }
*/

    public static OurLife updateOurlife(OurLife ourLife, UpdateOurlifeRequest request) {
        ourLife.contents = request.getContents();
        return ourLife;
    }

}
