package id.co.anfal.bsn.controller;

import id.co.anfal.bsn.dto.FeedbackRequest;
import id.co.anfal.bsn.dto.FeedbackResponse;
import id.co.anfal.bsn.dto.PageResponse;
import id.co.anfal.bsn.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback")
@Slf4j
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Long> saveFeedback(@Valid @RequestBody FeedbackRequest req, Authentication connectedUser) {
        log.info("Incoming saveFeedback");
        return ResponseEntity.ok(feedbackService.save(req, connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbacksByBook(
            @PathVariable("book-id") Long bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        log.info("Incoming findAllFeedbacksByBook");
        return ResponseEntity.ok(feedbackService.findAllFeedbackByBook(bookId, page, size, connectedUser));
    }
}