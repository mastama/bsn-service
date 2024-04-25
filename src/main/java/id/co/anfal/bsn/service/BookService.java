package id.co.anfal.bsn.service;

import id.co.anfal.bsn.common.BookSpecification;
import id.co.anfal.bsn.dto.BookRequest;
import id.co.anfal.bsn.dto.BookResponse;
import id.co.anfal.bsn.dto.BorrowedBookResponse;
import id.co.anfal.bsn.dto.PageResponse;
import id.co.anfal.bsn.entity.Book;
import id.co.anfal.bsn.entity.BookTransactionHistory;
import id.co.anfal.bsn.entity.User;
import id.co.anfal.bsn.exception.OperationNotPermittedException;
import id.co.anfal.bsn.repository.BookRepository;
import id.co.anfal.bsn.repository.BookTransactionHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Long save(BookRequest req, Authentication connectedUser) {
        log.info("Start save book: {}", req.id());
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(req);
        book.setOwner(user);
        log.info("End save book: {}", req.id());
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Long bookId) {
        log.info("Start Find Book by id: {}", bookId);
        log.info("End find Book by id: {}", bookId);
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the id: " + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        log.info("Start Find All Books");
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        log.info("End Find All Books");
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                (int) books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        log.info("Start Find All Books by owner");
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        log.info("End Find All Books by owner");
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                (int) books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        log.info("Start Find All Borrowed Books");
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> borrowedBookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        log.info("End Find All Borrowed Books");
        return new PageResponse<>(
                borrowedBookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                (int) allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        log.info("Start Find All Returned Books");
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> borrowedBookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        log.info("End Find All Returned Books");
        return new PageResponse<>(
                borrowedBookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                (int) allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Long updateShareableStatus(Long bookId, Authentication connectedUser) {
        log.info("Start Update Shareable Status");
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));
        User user = (User) connectedUser.getPrincipal();
        // Periksa apakah pemilik buku adalah pengguna yang terautentikasi
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update others books shareable status");
        }
        // Mengubah status shareable dan menyimpan perubahan ke dalam database
        book.setShareable(!book.getShareable());
        bookRepository.save(book);
        log.info("End Update Shareable Status");
        return bookId;
    }

    public Long updateArchivedStatus(Long bookId, Authentication connectedUser) {
        log.info("Start Update Archived Status");
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));
        User user = (User) connectedUser.getPrincipal();
        // Periksa apakah pemilik buku adalah pengguna yang terautentikasi
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update others books shareable status");
        }
        // Mengubah status archived dan menyimpan perubahan ke dalam database
        book.setArchived(!book.getArchived());
        bookRepository.save(book);
        log.info("End Update Archived Status");
        return bookId;
    }

    public Long borrowBook(Long bookId, Authentication connectedUser) {
        log.info("Start Borrow Book: {}", bookId);
        // check book on db
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));
        // if book archived or not shareable
        if (Boolean.TRUE.equals(book.getArchived()) || Boolean.TRUE.equals(!book.getShareable())) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not sharable");
        }
        // check book it is not your own book
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }
        // book isAlready borrow or not
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        log.info("End Borrow Book: {}", bookId);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Long returnBorrowedBook(Long bookId, Authentication connectedUser) {
        log.info("Start Return Book: {}", bookId);
        // check book in db
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));
        // check if book archived or not shareable
        if (Boolean.TRUE.equals(book.getArchived()) || Boolean.TRUE.equals(!book.getShareable())) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not sharable");
        }
        // check if user is it not returned own book
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow and return your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findBookByIdAndByUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow and returned this book"));
        bookTransactionHistory.setReturned(true);
        log.info("End Return Book: {}", bookId);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Long approveReturnBorrowedBook(Long bookId, Authentication connectedUser) {
        log.info("Start Approve Return Book: {}", bookId);
        // check book in db
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));
        // check if book archived or not shareable
        if (Boolean.TRUE.equals(book.getArchived()) || Boolean.TRUE.equals(!book.getShareable())) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not sharable");
        }
        // check if user is it not returned own book
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow and return your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findBookByIdAndByOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve its return"));
        bookTransactionHistory.setReturnApproved(true);
        log.info("End Approve Return Book: {}", bookId);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Long bookId) {
        log.info("Start Upload Cover Picture");
        // check book in db
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));
        User user = (User) connectedUser.getPrincipal();
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
    }
}
