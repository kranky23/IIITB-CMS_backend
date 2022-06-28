package com.spe.iiitbcms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long postId;
    private String subpostName;
    private String postName;
    private String url;
    private String description;
    private String email;
    private LocalDateTime localDateTime;
}
