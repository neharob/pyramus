package fi.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Nationality
 * 
 * @author antti.viljakainen
 */

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.READ_WRITE)
public class Nationality implements ArchivableEntity {

  /**
   * Returns internal unique id.
   * 
   * @return Internal unique id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets context dependent nationality code for this object.
   *  
   * @param code Nationality code in a context
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Returns context dependent nationality code.
   * 
   * @return Nationality code
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets user friendly name for this nationality.
   * 
   * @param name User friendly name for this nationality
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns user friendly name of this nationality.
   * 
   * @return User friendly name of this nationality
   */
  public String getName() {
    return name;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getArchived() {
    return archived;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Nationality")  
  @TableGenerator(name="Nationality", allocationSize=1)
  @DocumentId
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  @Field (index = Index.TOKENIZED)
  private String code;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field (index = Index.TOKENIZED) 
  private String name;
  
  @NotNull
  @Column (nullable = false)
  @Field (index = Index.TOKENIZED)
  private Boolean archived = Boolean.FALSE;

  @Version
  @NotNull
  @Column(nullable = false)
  private Long version;
}
