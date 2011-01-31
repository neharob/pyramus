package fi.pyramus.plugin.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.pyramus.PyramusRuntimeException;
import fi.pyramus.plugin.PluginDescriptor;
import fi.pyramus.plugin.PluginVault;

/**
 * The class responsible of managing the authentication providers of the application.
 */
public class AuthenticationProviderVault {
  
  /**
   * Returns a singleton instance of this class.
   * 
   * @return A singleton instance of this class
   */
  public static AuthenticationProviderVault getInstance() {
    return instance;
  }
  
 
  /**
   * Returns a collection of all authentication providers registered to this class.
   * 
   * @return A collection of all authentication providers registered to this class
   */
  public Collection<AuthenticationProvider> getAuthenticationProviders() {
    return authenticationProviders.values();
  }
  
  public List<InternalAuthenticationProvider> getInternalAuthenticationProviders() {
    List<InternalAuthenticationProvider> internalAuthenticationProviders = new ArrayList<InternalAuthenticationProvider>();
    for (AuthenticationProvider authenticationProvider : getAuthenticationProviders()) {
      if (authenticationProvider instanceof InternalAuthenticationProvider)
        internalAuthenticationProviders.add((InternalAuthenticationProvider) authenticationProvider);
    }
    return internalAuthenticationProviders;
  }
  
  public List<ExternalAuthenticationProvider> getExternalAuthenticationProviders() {
    List<ExternalAuthenticationProvider> externalAuthenticationProviders = new ArrayList<ExternalAuthenticationProvider>();
    for (AuthenticationProvider authenticationProvider : getAuthenticationProviders()) {
      if (authenticationProvider instanceof ExternalAuthenticationProvider)
        externalAuthenticationProviders.add((ExternalAuthenticationProvider) authenticationProvider);
    }
    
    return externalAuthenticationProviders;
  }
  
  public static Map<String, Class<AuthenticationProvider>> getAuthenticationProviderClasses() {
    return authenticationProviderClasses;
  }
  
  public boolean hasExternalStrategies() {
    return getExternalAuthenticationProviders().size() > 0;
  }
  
  public boolean hasInternalStrategies() {
    return getInternalAuthenticationProviders().size() > 0;
  }
  
  /**
   * Returns the authentication provider corresponding to the given name. If it doesn't exists, returns <code>null</code>.
   * 
   * @param name The authentication provider name
   * 
   * @return The authentication provider corresponding to the given name, or <code>null</code> if not found
   */
  public AuthenticationProvider getAuthenticationProvider(String name) {
    return authenticationProviders.get(name);
  }
  
  /**
    Registers the various authentication providers to this class.
  **/
  public void initializeStrategies() {
    String strategiesConf = System.getProperty("authentication.enabledStrategies");
    if ((strategiesConf == null)||("".equals(strategiesConf)))
      strategiesConf = "internal";
    
    String[] strategies = strategiesConf.split(",");
    for (String strategyName : strategies) {
      AuthenticationProvider provider;
      try {
        provider = authenticationProviderClasses.get(strategyName.trim()).newInstance();
        registerAuthenticationProvider(provider);
      } catch (InstantiationException e) {
        throw new PyramusRuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new PyramusRuntimeException(e);
      }
    }
  }
  
  /**
   * Registers an authentication provider to this class.
   * 
   * @param authenticationProvider The authentication provider to be registered
   */
  private void registerAuthenticationProvider(AuthenticationProvider authenticationProvider) {
    authenticationProviders.put(authenticationProvider.getName(), authenticationProvider);  
  }
  
  /** Map containing authentication provider names as keys and the providers themselves as values */ 
  private Map<String, AuthenticationProvider> authenticationProviders = new HashMap<String, AuthenticationProvider>();
  
  @SuppressWarnings("unchecked")
  public static void registerAuthenticationProviderClass(String name, Class<?> class1) {
    authenticationProviderClasses.put(name, (Class<AuthenticationProvider>) class1);
  }
  
  /** The singleton instance of this class */
  private static AuthenticationProviderVault instance = new AuthenticationProviderVault();
  /** All registered authentication provider classes **/
  private static Map<String, Class<AuthenticationProvider>> authenticationProviderClasses = new HashMap<String, Class<AuthenticationProvider>>();
  
  static {
    List<PluginDescriptor> plugins = PluginVault.getInstance().getPlugins();
    for (PluginDescriptor plugin : plugins) {
      if (plugin.getAuthenticationProviders() != null) {
        Map<String, Class<?>> authenticationProviders = plugin.getAuthenticationProviders();
        for (String authenticationProviderName : authenticationProviders.keySet()) {
          registerAuthenticationProviderClass(authenticationProviderName, authenticationProviders.get(authenticationProviderName));
        }
      }
    }
  }
}
