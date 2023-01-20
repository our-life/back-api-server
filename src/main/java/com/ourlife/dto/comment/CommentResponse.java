package com.ourlife.dto.comment;

import com.ourlife.dto.ourlife.OurlifeResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class CommentResponse {

    private String message;

    public static CommentResponse response(String message){
        return CommentResponse.builder()
                .message(message)
                .build();
    }
}
