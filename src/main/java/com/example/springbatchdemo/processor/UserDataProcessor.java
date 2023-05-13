package com.example.springbatchdemo.processor;

import com.example.springbatchdemo.model.UserCsv;
import com.example.springbatchdemo.model.UserH2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class UserDataProcessor implements ItemProcessor<UserCsv, UserH2> {

    private static final String MALE = "MALE";
    private static final char M = 'M';
    private static final char F = 'F';

    @Override
    public UserH2 process(UserCsv item) throws Exception {
        UserH2 userH2 = new UserH2();
        userH2.setId(item.getUserId());
        userH2.setFirstName(item.getFirstName().toUpperCase());
        userH2.setLastName(item.getLastName().toUpperCase());
        userH2.setGender(getGender(item.getSex()));
        userH2.setEmail(item.getEmail());
        userH2.setAge(getUserAge(item.getDateOfBirth()));
        userH2.setJobTitle(item.getJobTitle());
        return userH2;
    }

    private int getUserAge(LocalDate dateOfBirth) {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    private char getGender(String sex) {
        return sex.toUpperCase().equals(MALE) ? M : F;
    }
}
