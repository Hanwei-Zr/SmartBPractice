package wen.SmartBPractice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Client")
@ApiModel(description = "Client Model")
public class Client implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    @Column
    @ApiModelProperty(value = "Name", required = true)
    private String name;

    @Column
    private String email;

    @Column
    private String phone;

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