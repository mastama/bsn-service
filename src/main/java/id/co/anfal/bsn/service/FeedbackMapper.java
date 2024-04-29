package id.co.anfal.bsn.service;

import id.co.anfal.bsn.dto.FeedbackRequest;
import id.co.anfal.bsn.dto.FeedbackResponse;
import id.co.anfal.bsn.entity.Book;
import id.co.anfal.bsn.entity.Feedback;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest req) {
        return Feedback.builder()
                .note(req.note())
                .comment(req.comment())
                .book(Book.builder()
                        .id(Long.valueOf(req.bookId()))
                        .archived(false) //not required and has no impact :: just to satisfy lombok
                        .shareable(false) //not required and has no impact :: just to satisfy lombok
                        .build()
                )
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Long id) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedBack(Objects.equals(feedback.getCreatedBy(), id))
                .build();
    }
}
