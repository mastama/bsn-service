package id.co.anfal.bsn.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {
    private Double note;
    private String comment;
    private boolean ownFeedBack;
}
