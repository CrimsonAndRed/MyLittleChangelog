package my.little.changelog.controller;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.json.JsonDto;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Singleton
public class TestController {

    public List<String> test(Request req, Response res) {

        List<String> response = Lists.newArrayList();

        response.add("Test string 1");
        LocalDateTime currTime = LocalDateTime.now();
        response.add("Today is " + currTime.getYear() + " year");
        return response;
    }
}
