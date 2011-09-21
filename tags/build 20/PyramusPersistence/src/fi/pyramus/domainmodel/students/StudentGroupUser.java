package fi.pyramus.domainmodel.students;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.IndexedEmbedded;

import fi.pyramus.domainmodel.users.User;

@Entity
public class StudentGroupUser {

  /**
   * Returns unique identifier for this StudentGroupUser
   * 
   * @return unique id of this StudentGroupUser
   */
  public Long getId() {
    return id;
  }
  
  protected void setStudentGroup(StudentGroup studentGroup) {
    this.studentGroup = studentGroup;
  }

  public StudentGroup getStudentGroup() {
    return studentGroup;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentGroupUser")  
  @TableGenerator(name="StudentGroupUser", allocationSize=1)
  @DocumentId
  private Long id;

  @OneToOne
  @JoinColumn (name = "studentGroup")
  private StudentGroup studentGroup;

  @ManyToOne
  @JoinColumn (name = "user")
  @IndexedEmbedded
  private User user;  

  @Version
  @Column(nullable = false)
  private Long version;
}
