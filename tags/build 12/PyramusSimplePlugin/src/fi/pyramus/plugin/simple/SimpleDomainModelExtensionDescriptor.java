package fi.pyramus.plugin.simple;

import java.util.HashSet;
import java.util.Set;

import fi.pyramus.domainmodel.DomainModelExtensionDescriptor;
import fi.pyramus.plugin.simple.domainmodel.users.SimpleAuth;

public class SimpleDomainModelExtensionDescriptor implements DomainModelExtensionDescriptor {

  @Override
  public Set<Class<?>> getEntityClasses() {
    Set<Class<?>> entities = new HashSet<Class<?>>();
    
    entities.add(SimpleAuth.class);
    
    return entities;
  }
  
}