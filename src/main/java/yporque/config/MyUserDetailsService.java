package yporque.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yporque.model.Vendedor;
import yporque.repository.VendedorRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    public static final String ADMINISTRADOR = "Administrador";

    @Autowired
    private VendedorRepository vendedorRepository;

    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {


        List<Vendedor> vendedorList = vendedorRepository.findByUsername(username);
        if(!vendedorList.isEmpty()) {
            Vendedor user = vendedorList.get(0);
            List<GrantedAuthority> authorities;
            User u = null;
            if (ADMINISTRADOR.equals(user.getUsername())) {
                authorities =
                        buildUserAuthority(ADMINISTRADOR);
                u = buildUserForAuthentication(user, authorities);
            }
            return u;
        }else {
            throw new RuntimeException("Username not found");
        }

    }

    private User buildUserForAuthentication(Vendedor user,
                                            List<GrantedAuthority> authorities) {
        return new User(user.getUsername(), user.getPassword(),
                true, true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(String userRole) {

        Set<GrantedAuthority> setAuths = new HashSet<>();

        // Build user's authorities
        setAuths.add(new SimpleGrantedAuthority(userRole));

        return new ArrayList<>(setAuths);
    }

}