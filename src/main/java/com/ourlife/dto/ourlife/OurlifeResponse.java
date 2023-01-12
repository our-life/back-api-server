package com.ourlife.dto.ourlife;

import com.ourlife.entity.OurlifeLike;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OurlifeResponse {

    private String message;

    public static OurlifeResponse response(String message){
        return OurlifeResponse.builder()
                .message(message)
                .build();
    }
}
