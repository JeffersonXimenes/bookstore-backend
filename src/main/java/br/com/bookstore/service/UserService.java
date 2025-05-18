package br.com.bookstore.service;

import br.com.bookstore.enums.ProfileEnum;
import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.exception.ResourceValidationException;
import br.com.bookstore.mapper.UserMapper;
import br.com.bookstore.model.entity.CustomUserDetails;
import br.com.bookstore.model.entity.RoleEntity;
import br.com.bookstore.model.entity.UserEntity;
import br.com.bookstore.repository.RoleRepository;
import br.com.bookstore.repository.UserRepository;
import br.com.bookstore.utils.Constants;
import br.com.bookstore.validator.password.impl.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordValidator passwordValidator;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND));

        return new CustomUserDetails(userEntity.getEmail(), userEntity.getPassword(), userEntity.getName(), mapRolesToAuthorities(userEntity.getRoles()));
//        return new User(userEntity.getEmail(), userEntity.getPassword(), mapRolesToAuthorities(userEntity.getRoles()));
    }
    
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getDescription()))
                .collect(Collectors.toList());
    }

    public void save(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResourceValidationException(Collections.singletonList(Constants.USER_ALREADY_REGISTERED));
        }

        List<String> errorsFound = passwordValidator.getValidationErrors(password);
        if (!errorsFound.isEmpty()) {
            throw new ResourceValidationException(errorsFound);
        }

        UserEntity userEntity = userMapper.toEntity(email, name, passwordEncoder.encode((password)));

        String roleDescription = ProfileEnum.USER.getDescription();
        RoleEntity roles = roleRepository.findByDescription(roleDescription)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.ROLE_NOT_FOUND, roleDescription)));

        userEntity.setRoles(Collections.singletonList(roles));

        userRepository.save(userEntity);
    }
}