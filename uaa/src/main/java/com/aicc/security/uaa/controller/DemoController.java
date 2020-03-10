package com.aicc.security.uaa.controller;

import com.aicc.security.uaa.entity.LoginUserDTO;
import com.aicc.security.uaa.service.UserService;
import com.aicc.security.uaa.vo.LoginUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@Api(tags = "demo测试controller")
@RequestMapping("/demo")
public class DemoController {
    private static Logger logger = LoggerFactory.getLogger(DemoController.class);


    @ApiOperation("GET示例")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "queryParam", value = "demo-query-param", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "pathParam", value = "demo-path-param", required = true),
    })
    @GetMapping("/demoGet/{pathParam}")
    public String demoGetRole(
            @NotBlank(message = "{required}") String queryParam,
            @NotBlank(message = "{required}") @PathVariable String pathParam
    ) {
        try {
            System.out.println(queryParam);
            System.out.println(pathParam);
            LoginUserDTO loginUserDTO = new LoginUserDTO();
            loginUserDTO.setUserName("admin");
            loginUserDTO.setPassword("$2a$10$buyHUHK41XVCFOm64SyxGOcdpk7qviUrTmHZzyPHWrZX0yczObEI6");
            LoginUserVO login = userService.login(loginUserDTO);
            System.out.println(login);
//            return demoService.getRole().toString();
        } catch (Exception e) {
            logger.error("",e);
        }
        return null;
    }


    @ApiOperation("POST示例")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "queryParam", value = "demo-param", required = true),
    })
    @PostMapping("/demoPost")
    public String demoPost(
            @NotBlank(message = "{required}") String queryParam
    ) {
        try {
            System.out.println(queryParam);
        } catch (Exception e) {
            logger.error("",e);
        }
        return "demo test";
    }

    @Autowired
    private UserService userService;

    @ApiOperation("P示例")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "userName", value = "demo-param", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "password", value = "demo-param", required = true)
    })
    @GetMapping("check")
    public void check(
            @NotBlank(message = "{required}") String userName,
            @NotBlank(message = "{required}") String password
    ) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUserName(userName);
        loginUserDTO.setPassword(password);
        System.out.println(userName);
        LoginUserVO login = userService.login(loginUserDTO);
//        return ResultUtils.success(login);
        System.out.println(login);
    }

}
