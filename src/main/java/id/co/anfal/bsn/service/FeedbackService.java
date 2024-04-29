package id.co.anfal.bsn.service;

import id.co.anfal.bsn.dto.FeedbackRequest;
import id.co.anfal.bsn.dto.FeedbackResponse;
import id.co.anfal.bsn.dto.PageResponse;
import id.co.anfal.bsn.entity.Book;
import id.co.anfal.bsn.entity.Feedback;
import id.co.anfal.bsn.entity.User;
import id.co.anfal.bsn.exception.OperationNotPermittedException;
import id.co.anfal.bsn.repository.BookRepository;
import id.co.anfal.bsn.repository.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Long save(FeedbackRequest req, Authentication connectedUser) {
        Book book = bookRepository.findById(Long.valueOf(req.bookId()))
                .orElseThrow(() -> new EntityNotFoundException("No Book found with the ID: " + req.bookId()));
        if (Boolean.TRUE.equals(book.getArchived()) || Boolean.TRUE.equals(!book.getShareable())) {
            throw new OperationNotPermittedException("You cannot give a feedback for an archived or shareable book");
        }
        User user = (User) connectedUser;
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot give a feedback to your own book");
        }
        Feedback feedBack = feedbackMapper.toFeedback(req);
        return feedbackRepository.save(feedBack).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbackByBook(Long bookId, int page, int size, Authentication connectedUser) {
        log.info("Start findAllbackByBook: {}", bookId);
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser;
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                (int) feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
