package id.co.anfal.bsn.service;

import id.co.anfal.bsn.dto.BookRequest;
import id.co.anfal.bsn.dto.BookResponse;
import id.co.anfal.bsn.dto.BorrowedBookResponse;
import id.co.anfal.bsn.entity.Book;
import id.co.anfal.bsn.entity.BookTransactionHistory;
import id.co.anfal.bsn.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookMapper {
    public Book toBook(BookRequest req) {
        log.info("Start process toBook req: {}", req.id());
        log.info("End process toBook req: {}", req.id());
        return Book.builder()
                .id(req.id())
                .title(req.title())
                .author(req.authorName())
                .archived(false)
                .shareable(req.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        log.info("Start process build toBookResponse");
        log.info("End process build toBookResponse");
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.getArchived())
                .shareable(book.getShareable())
                .owner(book.getOwner().getFullName())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookTransactionHistory) {
        log.info("Start process build toBorrowedBookResponse");
        log.info("End process build toBorrowedBookResponse");
        return BorrowedBookResponse.builder()
                .id(bookTransactionHistory.getBook().getId())
                .title(bookTransactionHistory.getBook().getTitle())
                .author(bookTransactionHistory.getBook().getAuthor())
                .isbn(bookTransactionHistory.getBook().getIsbn())
                .rate(bookTransactionHistory.getBook().getRate())
                .returned(bookTransactionHistory.isReturned())
                .returnedApproved(bookTransactionHistory.isReturnApproved())
                .build();
    }
}
