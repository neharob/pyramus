package fi.pyramus.domainmodel.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class AcademicTerm {

  public Long getId() {
    return id;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getArchived() {
    return archived;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="AcademicTerm")  
  @TableGenerator(name="AcademicTerm", allocationSize=1)
  private Long id;

  @Column (nullable=false)
  @NotEmpty
  private String name;

  @Column (nullable=false)
  @Temporal (value=TemporalType.DATE)
  private Date startDate;
  
  @Column (nullable=false)
  @Temporal (value=TemporalType.DATE)
  private Date endDate;
  
  @NotNull
  @Column(nullable = false)
  @Field (index=Index.UN_TOKENIZED)
  private Boolean archived = Boolean.FALSE;

}
