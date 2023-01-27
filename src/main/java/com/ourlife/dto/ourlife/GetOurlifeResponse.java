package com.ourlife.dto.ourlife;

import com.ourlife.entity.OurLife;
import lombok.*;

import java.util.List;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetOurlifeResponse {

    private String userEmail;

    private String contents;

    private List<String> ImgList;

    private int ourlifeLikeCount;

    //댓글 갯수, 좋아요 갯수

    public static GetOurlifeResponse from(OurLife ourLife, List<String> imgUrl, int likeCount) {
        return GetOurlifeResponse.builder()
                .userEmail(ourLife.getUser().getEmail())
                .contents(ourLife.getContents())
                .ImgList(imgUrl)
                .ourlifeLikeCount(likeCount)
                .build();
    }

}
