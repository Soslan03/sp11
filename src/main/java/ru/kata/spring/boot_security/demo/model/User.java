package ru.kata.spring.boot_security.demo.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@Table(name = "accounts")
@DynamicUpdate(value = true) // По умолчанию Hibernate если мы обновляем только одно поле.
// То он все равно обновляет все поля. Он делает это, потому что с нами параллельно может кто то работать с этим обьектом,
// и мы могли получить не то что хотели. По этому Hibernate дает нам возможность вк флажки. Мы говорим. Меняй только те поля,
// которые я обновляю
//@DynamicInsert(value = true)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name should be not Empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Second name should be not Empty")
    @Size(min = 2, max = 30, message = "Second name should be between 2 and 30 characters")
    @Column(name = "second_name")
    private String second_name;

    @Min(value = 0, message = "Age should be greater then 0")
    @Column(name = "age")
    private int age;

    @NotEmpty(message = "Email should be not Empty")
    @Email(message = "Email should be valid ")
    @Column(name = "email")
    private String email;
    @Column
    @NotEmpty(message = "password should not be empty")
    private String password;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "users_role"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    public User() {

    }
    public void removeRole(Role role) {
        roles.remove(role);
        role.getUser().remove(this);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //Предоставленные Полномочия
        return roles;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }


    @Override
    public boolean isAccountNonExpired() { //срок Действия Учетной Записи Не Истек
        return false;
    } // срок Действия Учетной Записи Не Истек

    @Override
    public boolean isAccountNonLocked() { //не Заблокирована Ли Учетная запись
        return false;
    } // не Заблокирована Ли Учетная запись

    @Override
    public boolean isCredentialsNonExpired() { //не Истек Ли Срок Действия Учетных Данных
        return false;
    } // не Истек Ли Срок Действия Учетных Данных

    @Override
    public boolean isEnabled() { // включено
        return false;
    } //ВКЛЮЧЕН

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
