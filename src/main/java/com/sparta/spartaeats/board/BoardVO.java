package com.sparta.spartaeats.board;

import com.sparta.spartaeats.common.util.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "test_board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardVO extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String contents;



}
