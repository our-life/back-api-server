package com.ourlife.dto.ourlife;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOurlifeLikeResponse {

    List<String> nicknameList;


    public static GetOurlifeLikeResponse makeList(List<String> name){
        return GetOurlifeLikeResponse.builder()
                .nicknameList(name)
                .build();

    }

}
