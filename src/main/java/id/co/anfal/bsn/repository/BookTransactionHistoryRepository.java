package id.co.anfal.bsn.repository;

import id.co.anfal.bsn.entity.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {

    @Query("""
            select history from BookTransactionHistory history
            where history.user.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Long userId);

    @Query("""
            select history from BookTransactionHistory history
            where history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Long userId);

    @Query("""
            select (count(*) > 0) as isBorrowed
            from BookTransactionHistory bookTransactionHistory
            where bookTransactionHistory.user.id = :userId
            and bookTransactionHistory.book.id = :bookId
            and bookTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Long bookId, Long userId);

    @Query("""
            select transaction
            from BookTransactionHistory transaction
            where transaction.book.id = :bookId
            and transaction.user.id = :userId
            and transaction.returned = false
            and transaction.returnApproved = false
            """)
    /*
    mencari transaksi buku berdasarkan ID buku dan ID pengguna tertentu,
     dengan kondisi bahwa transaksi tersebut belum dikembalikan (returned = false) dan
      belum disetujui pengembaliannya (returnApproved = false):
     */
    Optional<BookTransactionHistory> findBookByIdAndByUserId(Long bookId, Long userId);

    @Query("""
            select transaction
            from BookTransactionHistory transaction
            where transaction.book.id = :bookId
            and transaction.book.owner.id = :userId
            and transaction.returned = false
            and transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findBookByIdAndByOwnerId(Long bookId, Long userId);
}
