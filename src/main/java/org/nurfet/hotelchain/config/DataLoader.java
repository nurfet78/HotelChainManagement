package org.nurfet.hotelchain.config;
import lombok.RequiredArgsConstructor;
import org.nurfet.hotelchain.model.Hotel;
import org.nurfet.hotelchain.model.Role;
import org.nurfet.hotelchain.model.Room;
import org.nurfet.hotelchain.model.User;
import org.nurfet.hotelchain.repository.RoleRepository;
import org.nurfet.hotelchain.repository.UserRepository;
import org.nurfet.hotelchain.service.HotelService;
import org.nurfet.hotelchain.service.RoomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoomService roomService;

    private final HotelService hotelService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        Role roleAdmin = addRole("ROLE_ADMIN");
        Role roleUser = addRole("ROLE_USER");
        addHotelAndRooms();
        addUser("Иван",
                "Иванов",
                "ivan@mail.ru",
                "+7(978)123-34-36",
                "ivan",
                roleUser);

        addUser("Петр",
                "Петров",
                "peter@mail.ru",
                "+7(978)542-67-89",
                "peter",
                roleUser);

        addUser("Евгения",
                "Лебедь",
                "evgeniya@mail.ru",
                "+7(978)598-21-77",
                "user",
                roleUser);

        addUser("Админ",
                "Админов",
                "admin@mail.ru",
                "+7(978)722-56-24",
                "admin",
                roleAdmin, roleUser);
    }

    private void addHotelAndRooms() {
        Hotel California = new Hotel(
                "Калифорния",
                "Москва",
                List.of("Wi-fi", "Завтрак", "Обед", "Ужин", "Бассейн", "Настольные игры", "Тренажерный зал"),
                new BigDecimal("4.5"),
                "Комфортабельный и элитный отель «Калифорния» расположился в " +
                        "столичной области в Красногорском районе возле леса, " +
                        "березовой рощи, и больших озер. Такой отдых понравится " +
                        "любому жителю мегаполиса, ведь он будет умиротворенным, " +
                        "приятным и расслабляющим.", Collections.emptyList()
        );

        Hotel Diamond = new Hotel(
                "Алмаз",
                "Казань",
                List.of("Бесплатный Wi-Fi,Завтрак,Кафе,Кондиционер,Размещение подходит для семей/детей"),
                new BigDecimal("4.5"),
                """
                        Гостиница «Алмаз» расположена в центре города Казани, вблизи памятников компасу и Казанскому коту, \
                        Петропавловского собора и национального музея Республики Татарстан.
                        
                        Для размещения предлагаются комфортные и уютные номера, каждый из которых \
                        оформлен в приятной цветовой гамме и включает в себя все необходимое.""", Collections.emptyList()
        );

        Hotel hotel1 = hotelService.createHotel(California);
        Hotel hotel2 = hotelService.createHotel(Diamond);

        Room Suite = new Room(
                "Люкс",
                new BigDecimal("60000.00"),
                "Интерьерный двухкомнатный люкс с джакузи высшей категории. " +
                        "Подходит для размещения от одного до четырёх гостей.",
                California
        );

        Room Family = new Room(
                "Стандарт",
                new BigDecimal("7800.00"),
                "Двухкомнатный Семейный номер высшей категории комфорт-класса " +
                        "Двухкомнатный номер отлично подходит для комфортного семейного отдыха.",
                California
        );

        Room Economy = new Room(
                "Эконом",
                new BigDecimal("3700.00"),
                "Недорогой номер первой категории Эконом для 1-2 гостей.",
                California
        );

        /////////////////////////////////////////////////
        Room Standard1 = new Room(
                "Стандарт",
                new BigDecimal("4996.00"),
                "Двуспальная кровать. Затемнённые шторы. Шкаф. Кондиционер. " +
                        "Холодильник. Отопление. Душ. Собственная ванная комната.",
                Diamond
        );

        Room Standard2 = new Room(
                "Стандарт",
                new BigDecimal("4996.00"),
                "2 отдельные кровати. Гладильные принадлежности. " +
                        "Собственная ванная комната. Туалетные принадлежности. Затемнённые шторы. Фен. Душ. Шкаф.",
                Diamond
        );

        Room Superior = new Room(
                "Двухместный номер Superior",
                new BigDecimal("6009.00"),
                "Двуспальная кровать. Затемнённые шторы. Фен. Отопление. Холодильник. " +
                        "Собственная ванная комната. Душ. Туалетные принадлежности.",
                Diamond
        );

        roomService.createRoom(hotel1.getId(), Suite);
        roomService.createRoom(hotel1.getId(), Family);
        roomService.createRoom(hotel1.getId(), Economy);

        roomService.createRoom(hotel2.getId(), Standard1);
        roomService.createRoom(hotel2.getId(), Standard2);
        roomService.createRoom(hotel2.getId(), Superior);

        California.setRooms(List.of(Suite, Family, Economy));
        hotelService.updateHotel(hotel1.getId(), California);

        Diamond.setRooms(List.of(Standard1, Standard2, Superior));
        hotelService.updateHotel(hotel2.getId(), Diamond);
    }

    private void addUser(String firstName, String lastName, String email,
                         String phone, String username, Role... roles) {

        userRepository.findByUsername(username).orElseGet(() -> {
            User user = new User(
                    firstName,
                    lastName,
                    email,
                    phone,
                    username,
                    passwordEncoder.encode(username),
                    new HashSet<>(Arrays.asList(roles)));

            return userRepository.save(user);
        });
    }

    private Role addRole(String roleName) {
        return roleRepository.findRoleByAuthority(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}
