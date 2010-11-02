package fi.pyramus.domainmodel.reports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class ReportCategory {

  public Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setIndexColumn(Integer indexColumn) {
    this.indexColumn = indexColumn;
  }

  public Integer getIndexColumn() {
    return indexColumn;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ReportCategory")  
  @TableGenerator(name="ReportCategory", allocationSize=1)
  private Long id;

  @NotNull
  @NotEmpty
  @Column (nullable = false)
  private String name;

  private Integer indexColumn;

  @Version
  @NotNull
  @Column(nullable = false)
  private Long version;
}