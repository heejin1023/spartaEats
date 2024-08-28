package com.sparta.spartaeats.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "test_board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardVO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String contents;
    private Date createdAt;
    private Date updatedAt;


}
