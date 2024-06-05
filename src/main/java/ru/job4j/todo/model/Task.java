package ru.job4j.todo.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToMany
    @JoinTable(
            name = "task_to_categories",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "categories_id")}
    )
    @Fetch(FetchMode.JOIN)
    private List<Category> categories = new ArrayList<>();
}
