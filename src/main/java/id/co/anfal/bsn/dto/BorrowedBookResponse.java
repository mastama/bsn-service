package id.co.anfal.bsn.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private double rate;
    private boolean returned;
    private boolean returnedApproved;
}
