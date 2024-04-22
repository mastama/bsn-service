package id.co.anfal.bsn.entity;

import id.co.anfal.bsn.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
public class BookTransactionHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id") //foreignKey
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id") //foreignKey
    private Book book;

    private boolean returned;
    private boolean returnApproved;
}
