package ru.job4j.todo.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    @EqualsAndHashCode.Include
    private String title;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
    private boolean done;
    @ManyToOne
    @JoinColumn(name = "todo_user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    @Fetch(FetchMode.JOIN)
    private Priority priority;
}
