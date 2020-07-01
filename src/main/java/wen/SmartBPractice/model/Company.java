package wen.SmartBPractice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Company")
public class Company implements Serializable {
    @Id
    @Column(name="comapny_id")
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String create_by;

    @Column(updatable = false)
    @CreationTimestamp
    private Date create_at;

    @Column
    private String update_by;

    @UpdateTimestamp
    private Date update_at;
}