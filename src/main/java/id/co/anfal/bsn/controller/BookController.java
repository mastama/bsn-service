package id.co.anfal.bsn.controller;

import id.co.anfal.bsn.dto.BookRequest;
import id.co.anfal.bsn.dto.BookResponse;
import id.co.anfal.bsn.dto.BorrowedBookResponse;
import id.co.anfal.bsn.dto.PageResponse;
import id.co.anfal.bsn.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Tag(name = "Book")
@Slf4j
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Long> saveBook(@RequestBody @Valid BookRequest req, Authentication connectedUser) {
        log.info("Incoming Save Book: {}", req.id());
        log.info("Outgoing Save Book: {}", req.id());
        return ResponseEntity.ok(bookService.save(req, connectedUser));
    }

    @GetMapping(value = "/{book-id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Long bookId) {
        log.info("Incoming findBookById: {}", bookId);
        log.info("Outgoing findBookById: {}", bookId);
        return ResponseEntity.ok(bookService.findById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        log.info("Incoming findAllBooks: {}", page);
        log.info("Outgoing findAllBooks: {}", size);
        return ResponseEntity.ok(bookService.findAllBooks(page, size, connectedUser));
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        log.info("Incoming findAllBooksByOwner: {}", page);
        log.info("Outgoing findAllBooksByOwner: {}", size);
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    @GetMapping(value = "/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        log.info("Incoming findAllBorrowedBooks: {}", page);
        log.info("Outgoing findAllBorrowedBooks: {}", size);
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping(value = "/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        log.info("Incoming findAllReturnedBooks: {}", page);
        log.info("Outgoing findAllReturnedBooks: {}", size);
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping(value = "/shareable/{book-id}")
    public ResponseEntity<Long> updateShareableStatus(@PathVariable("book-id") Long bookId, Authentication connectedUser) {
        log.info("Incoming updateShareableStatus: {}", bookId);
        log.info("Outgoing updateShareableStatus: {}", bookId);
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId, connectedUser));
    }

    @PatchMapping(value = "/archived/{book-id}")
    public ResponseEntity<Long> updateArchivedStatus(@PathVariable("book-id") Long bookId, Authentication connectedUser) {
        log.info("Incoming updateArchivedStatus: {}", bookId);
        log.info("Outgoing updateArchivedStatus: {}", bookId);
        return ResponseEntity.ok(bookService.updateArchivedStatus(bookId, connectedUser));
    }

    @PostMapping(value = "/borrow/{book-id}")
    public ResponseEntity<Long> borrowBook(@PathVariable("book-id") Long bookId, Authentication connectedUser) {
        log.info("Incoming borrowBook: {}", bookId);
        log.info("Outgoing borrowBook: {}", bookId);
        return ResponseEntity.ok(bookService.borrowBook(bookId, connectedUser));
    }

    @PatchMapping(value = "/borrow/return/{book-id}")
    public ResponseEntity<Long> returnBorrowedBook(@PathVariable("book-id") Long bookId, Authentication connectedUser) {
        log.info("Incoming returnBook: {}", bookId);
        log.info("Outgoing returnBook: {}", bookId);
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId, connectedUser));
    }

    @PatchMapping(value = "/borrow/return/approve/{book-id}")
    public ResponseEntity<Long> approveReturnBorrowedBook(@PathVariable("book-id") Long bookId, Authentication connectedUser) {
        log.info("Incoming approve return borrowed book: {}", bookId);
        log.info("Outgoing approve return borrowed book: {}", bookId);
        return ResponseEntity.ok(bookService.approveReturnBorrowedBook(bookId, connectedUser));
    }

    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") Long bookId,
            @Parameter() @RequestPart("file")
            MultipartFile file, Authentication connectedUser) {
        log.info("Incoming uploadBookCoverPicture: {}", bookId);
        log.info("Outgoing uploadBookCoverPicture: {}", bookId);
        bookService.uploadBookCoverPicture(file, connectedUser, bookId);
        return ResponseEntity.accepted().build();
    }
}
