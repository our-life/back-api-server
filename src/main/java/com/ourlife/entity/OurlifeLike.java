package com.ourlife.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OurlifeLike {

    @Id
    @Column(name = "ourlife_Like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OurLife ourLife;

    @ManyToOne
    private User user;


    public static OurlifeLike createAraLike(User user, OurLife ourLife){
        OurlifeLike araLike = new OurlifeLike();
        araLike.user = user;
        araLike.ourLife = ourLife;
        return araLike;
    }

}
