package id.co.anfal.bsn.entity;

import id.co.anfal.bsn.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {
    private Double note;
    private String comment;

    @ManyToOne // one book have many feedBacks
    @JoinColumn(name = "book_id") //foreignKey
    private Book book;
}