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
    private List<Feedback> feedBacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> bookTransactionHistories;

    @Transient
    public double getRate() {
        if (feedBacks == null || feedBacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedBacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        // 3.23 --> 3.0 || 3.65 --> 4.0
        double roundedRate = Math.round(rate * 10.0) / 10.0;
        return roundedRate;
    }

}
