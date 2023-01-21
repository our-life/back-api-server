package com.ourlife.dto.comment;

import lombok.Data;

@Data
public class UpdateCommentRequest {

    private Long araId;

    private Long commentId;

    private String contents;

}
