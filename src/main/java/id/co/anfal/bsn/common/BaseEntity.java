package id.co.anfal.bsn.common;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDate createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDate lastModifiedDate;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;
}
