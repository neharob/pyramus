package fi.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Education subtype to further define the education type it belongs to.
 */
@Entity
@Cache (usage = CacheConcurrencyStrategy.READ_WRITE)
public class EducationSubtype {
  
  /**
   * Default constructor.
   */
  protected EducationSubtype() {
  }

  /**
   * Constructor defining the education type this subtype belongs to.
   * 
   * @param educationType The education type of this subtype
   */
  public EducationSubtype(EducationType educationType) {
    setEducationType(educationType);
  }

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the name of this subtype.
   * 
   * @return The name of this subtype
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this subtype.
   * 
   * @param name The name of this sutype
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Returns the code of this education subtype
   * 
   * @return code of this education subtype
   */
  public String getCode() {
    return code;
  }
  
  /**
   * Sets the code of this education subtype.
   * 
   * @param name the code of this education subtype
   */
  public void setCode(String code) {
    this.code = code;
  }
  
  /**
   * Returns the education type of this subtype.
   * 
   * @return The education type of this subtype
   */
  public EducationType getEducationType() {
    return educationType;
  }
  
  /**
   * Sets the education type of this subtype.
   * 
   * @param educationType The education type of this subtype
   */
  protected void setEducationType(EducationType educationType) {
    this.educationType = educationType;
  }

  /**
   * Sets the archived flag of this object.
   * 
   * @param archived The archived flag of this object
   */
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  /**
   * Returns the archived flag of this object.
   * 
   * @return The archived flag of this object
   */
  public Boolean getArchived() {
    return archived;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="EducationSubtype")  
  @TableGenerator(name="EducationSubtype", allocationSize=1)
  private Long id;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  private String name;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field (index = Index.TOKENIZED)
  private String code;

  @ManyToOne
  @JoinColumn(name="educationType")
  private EducationType educationType;

  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

}
