package fi.pyramus.dao.students;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.students.StudentVariableKey;
import fi.pyramus.domainmodel.students.StudentVariableKey_;

public class StudentVariableKeyDAO extends PyramusEntityDAO<StudentVariableKey> {

  public StudentVariableKey findByKey(String key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentVariableKey> criteria = criteriaBuilder.createQuery(StudentVariableKey.class);
    Root<StudentVariableKey> root = criteria.from(StudentVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentVariableKey_.variableKey), key)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  /**
   * Returns a list of user editable student variable keys from the database, sorted by their user interface name.
   * 
   * @return A list of user editable student variable keys
   */
  public List<StudentVariableKey> listUserEditableStudentVariableKeys() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentVariableKey> criteria = criteriaBuilder.createQuery(StudentVariableKey.class);
    Root<StudentVariableKey> root = criteria.from(StudentVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentVariableKey_.userEditable), Boolean.TRUE)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}