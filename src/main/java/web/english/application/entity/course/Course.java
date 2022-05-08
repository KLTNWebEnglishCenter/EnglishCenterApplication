package web.english.application.entity.course;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    private int id;
    private String name;
    private String description;
    private double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate modifiedDate;
    private float discount;
    private boolean enable;

    private Level level;

    private Category category;

    public Course(@NonNull String name, @NonNull double price) {
        this.name = name;
        this.price = price;
        this.createDate=LocalDate.now();
        this.enable=true;
    }

    public Course(int id, String name, String description, double price, LocalDate createDate, LocalDate modifiedDate, float discount, Level level, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.discount = discount;
        this.level = level;
        this.category = category;
        this.enable = true;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                ", discount=" + discount +
                ", enable=" + enable +
                ", level=" + level +
                ", category=" + category +
                '}';
    }
}
