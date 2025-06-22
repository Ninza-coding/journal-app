package com.techn.journalapp.service;

import com.techn.journalapp.entity.User;
import com.techn.journalapp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Disabled
 //   @Test
    public void testFindByUserName(){
    assertNotNull(userRepository.findByUserName("ram"));
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsPrivider.class)
    public void userSaveTest(User user){
    assertTrue(userService.saveNewUser(user));
    }


    @Disabled
   // @ParameterizedTest
  //  @ValueSource(strings = { give the string here or int or Long change key first}) // any one can be used.
    @CsvSource({
            "ram",
            "vipul",
            "irshd",
            "akshit"

    })
    public void testFindByUserName(String userName){
        assertNotNull(userRepository.findByUserName(userName));
    }

    @Disabled
   // @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "3,4,7",
            "4,5,10"

    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a+b);
    }

}
