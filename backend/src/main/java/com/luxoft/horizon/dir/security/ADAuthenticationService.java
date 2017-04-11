package com.luxoft.horizon.dir.security;

/**
 * Created by bogatp on 13.04.16.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class ADAuthenticationService {

    public static final Logger logger = Logger.getLogger(ADAuthenticationService.class.getName());
    public static final String GROUP_TEMPLATE = "(&(objectClass=group)(cn={0}))";

    @Value("${ad.url}")
    private String ldapHost;

    @Value("${ad.search.base}")
    private String searchBase;

    @Value("${ad.group.admin}")
    private String adGroupAdmin;

    @Value("${ad.group.users}")
    private String adGroupUser;

    private LdapContext context;

    public boolean isAdmin(String user) {
        return isGroupMember(user, MessageFormat.format(GROUP_TEMPLATE, adGroupAdmin));
    }

    public boolean isUser(String user) {
        return isGroupMember(user, MessageFormat.format(GROUP_TEMPLATE, adGroupUser));
    }

    public boolean isGroupMember(String user, String group) {
        String returnedAtts[] = {"*"};
        String searchFilter = group;

        //Create the search controls
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAtts);

        //Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        Set<String> set = new HashSet<>();
        try {
            //Search objects in GC using filters
            NamingEnumeration answer = context.search(searchBase, searchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                if (attrs != null) {
                    Attribute members = attrs.get("member");
                    NamingEnumeration ne = members.getAll();
                    while (ne.hasMore()) {
                        //TODO Parse
                        String str = (String) ne.next();
                        String cn = str.split(",")[0];
                        set.add(cn.substring(3, cn.length()).toUpperCase());
                    }
                    ne.close();
                }
            }
        } catch (Exception ex) {
            logger.warning(MessageFormat.format("AD error: ", ex.getMessage()));
            return false;
        }
        return set.contains(user.toUpperCase());
    }

    public boolean authenticate(String user, String domain, String pass) {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapHost);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, pass);
        try {
            this.context = new InitialLdapContext(env, null);
            //TODO distinguish exceptions
        } catch (NamingException ex) {
            logger.warning(MessageFormat.format("Authentication error, user {0}/{1}, caused by:  ", domain, user, ex.getMessage()));
            return false;
        }
        return true;
    }

}
