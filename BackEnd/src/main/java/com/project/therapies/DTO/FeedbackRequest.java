package com.project.therapies.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackRequest {
    private String comment;
    private Long userId;
    private Long therapyId;
}
