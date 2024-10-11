package org.nurfet.hotelchain.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nurfet.hotelchain.validation.ValidationGroup;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов", groups = ValidationGroup.Default.class)
    @NotBlank(message = "Имя должно быть указано", groups = ValidationGroup.Default.class)
    private String firstName;

    @Size(min = 2, max = 30, message = "Фамилия должна содержать от 2 до 30 символов", groups = ValidationGroup.Default.class)
    @NotBlank(message = "Фамилия должна быть указана", groups = ValidationGroup.Default.class)
    private String lastName;

    @Pattern(regexp = "^(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+$", message = "Адрес электронной почты указан неверно", groups = ValidationGroup.Default.class)
    @NotBlank(message = "Поле email должно быть заполнено", groups = ValidationGroup.Default.class)
    private String email;

    @NotBlank(message = "Поле телефон должно быть заполнено", groups = ValidationGroup.Default.class)
    private String phone;

    @Size(min = 3, max = 30, message = "Имя пользователя должно содержать от 3 до 30 символов", groups = ValidationGroup.Default.class)
    @NotBlank(message = "Имя пользователя не должно быть пустым", groups = ValidationGroup.Default.class)
    private String username;

    @Size(min = 4, max = 8, message = "Пароль должен содержать от 4 до 8 символов", groups = ValidationGroup.Default.class)
    @NotBlank(message = "Пароль не должно быть пустым", groups = ValidationGroup.Default.class)
    private String password;

    @Size(min = 1, message = "Должна быть выбрана хотя бы одна роль", groups = ValidationGroup.Specific.class)
    private Set<String> roles = new HashSet<>();
}



