package com.example.springbatchdemo.listener;

import com.example.springbatchdemo.model.UserCsv;
import com.example.springbatchdemo.model.UserH2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Service;

@Service
public class UserErrorSkipListener implements SkipListener<UserCsv, UserH2> {

    private final Logger LOGGER = LoggerFactory.getLogger(UserProcessJobListener.class);


    @Override
    public void onSkipInRead(Throwable t) {
        if(t instanceof FlatFileParseException){
            LOGGER.warn("Parsing error occurred for the row {}",((FlatFileParseException) t).getInput());
        }
    }

    @Override
    public void onSkipInWrite(UserH2 item, Throwable t) {
        LOGGER.warn("Could not save user {} due to error {}",item,t.getMessage());
    }

    @Override
    public void onSkipInProcess(UserCsv item, Throwable t) {
        LOGGER.warn("Could not process user {} due to error {}",item,t.getMessage());
    }
}
