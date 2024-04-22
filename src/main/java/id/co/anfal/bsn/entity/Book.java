package id.co.anfal.bsn.entity;

import id.co.anfal.bsn.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {
    private String title;
    private String author;
    private String isbn;
    private String synopsis;
    private String bookCover;

    private Boolean archived;
    private Boolean shareable;

    @ManyToOne //many book can have one user
    @JoinColumn(name = "owner_id") //foreignKey
    private User owner;

    @OneToMany(mappedBy = "book") //one book can have many feedBacks
    private List<FeedBack> feedBacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> bookTransactionHistories;

}
