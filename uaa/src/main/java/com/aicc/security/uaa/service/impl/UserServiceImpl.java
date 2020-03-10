package com.aicc.security.uaa.service.impl;

import com.aicc.security.uaa.config.ServerConfig;
import com.aicc.security.uaa.dao.AuthRoleMapper;
import com.aicc.security.uaa.dao.AuthUserMapper;
import com.aicc.security.uaa.domain.Token;
import com.aicc.security.uaa.entity.AuthRole;
import com.aicc.security.uaa.entity.AuthUser;
import com.aicc.security.uaa.entity.LoginUserDTO;
import com.aicc.security.uaa.enums.ResponseEnum;
import com.aicc.security.uaa.enums.UrlEnum;
import com.aicc.security.uaa.service.RoleService;
import com.aicc.security.uaa.service.UserService;
import com.aicc.security.uaa.utils.BeanUtils;
import com.aicc.security.uaa.utils.RedisUtil;
import com.aicc.security.uaa.utils.SortableUUID;
import com.aicc.security.uaa.vo.LoginUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.aicc.security.uaa.config.OAuth2Config.*;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthRoleMapper authRoleMapper;

    @Autowired
    private AuthUserMapper authUserMapper;


    @Autowired
    private RoleService roleService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(AuthUser authUser)  {
        AuthUser userPO = new AuthUser();
        AuthUser userByAccount = authUserMapper.selectByUserName(authUser.getUser_name());
        if(userByAccount != null){
            //此处应该用自定义异常去返回
            try {
                throw new Exception("This user already exists!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userPO.setCreate_time(new Date());
        userPO.setUpdate_time(new Date());
        userPO.setId(SortableUUID.randomUUID());
        BeanUtils.copyPropertiesIgnoreNull(authUser, userPO);
        authUserMapper.insert(userPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String id)  {
        AuthUser userPO = authUserMapper.selectByPrimaryKey(id);
        if(userPO == null){
            //此处应该用自定义异常去返回
            try {
                throw new Exception("This user not exists!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        authUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(AuthUser userDTO) {
        AuthUser userPO = authUserMapper.selectByPrimaryKey(userDTO.getId());
        if(userPO == null){
            //此处应该用自定义异常去返回
            try {
                throw new Exception("This user not exists!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BeanUtils.copyPropertiesIgnoreNull(userDTO, userPO);
        authUserMapper.updateByPrimaryKey(userPO);
    }

    @Override
    public List<AuthUser> findAllUserVO() {
        List<AuthUser> userPOList = authUserMapper.selectAll();
        List<AuthUser> userVOList = new ArrayList<>();
        userPOList.forEach(userPO->{
            AuthUser userVO = new AuthUser();
            BeanUtils.copyPropertiesIgnoreNull(userPO,userVO);
            AuthRole roleVO = new AuthRole();
            BeanUtils.copyPropertiesIgnoreNull(userPO.getRole(),roleVO);
            userVO.setRole(roleVO);
            userVOList.add(userVO);
        });
        return userVOList;
    }

    @Override
    public LoginUserVO login(LoginUserDTO loginUserDTO) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", CLIENT_ID);
        paramMap.add("client_secret", CLIENT_SECRET);
        paramMap.add("username", loginUserDTO.getUserName());
        paramMap.add("password", loginUserDTO.getPassword());
        paramMap.add("grant_type", GRANT_TYPE[0]);
        Token token = null;
        try {
            //因为oauth2本身自带的登录接口是"/oauth/token"，并且返回的数据类型不能按我们想要的去返回
            //但是我的业务需求是，登录接口是"user/login"，由于我没研究过要怎么去修改oauth2内部的endpoint配置
            //所以这里我用restTemplate(HTTP客户端)进行一次转发到oauth2内部的登录接口，比较简单粗暴
            token = restTemplate.postForObject(serverConfig.getUrl() + UrlEnum.LOGIN_URL.getUrl(), paramMap, Token.class);
            LoginUserVO loginUserVO = redisUtil.get(token.getValue(), LoginUserVO.class);
            if(loginUserVO != null){
                //登录的时候，判断该用户是否已经登录过了
                //如果redis里面已经存在该用户已经登录过了的信息
                //我这边要刷新一遍token信息，不然，它会返回上一次还未过时的token信息给你
                //不便于做单点维护
                token = oauthRefreshToken(loginUserVO.getRefreshToken());
                redisUtil.deleteCache(loginUserVO.getAccessToken());
            }
        } catch (RestClientException e) {
            try {
                e.printStackTrace();
                //此处应该用自定义异常去返回，在这里我就不去具体实现了
                //throw new Exception("username or password error");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        //这里我拿到了登录成功后返回的token信息之后，我再进行一层封装，最后返回给前端的其实是LoginUserVO
        LoginUserVO loginUserVO = new LoginUserVO();

        AuthUser userPO = authUserMapper.selectByUserName(loginUserDTO.getUserName());
        //TODO 补充缓存信息
        loginUserVO.setPassword(userPO.getPassword());
        loginUserVO.setUserName(userPO.getUser_name());
        loginUserVO.setAccessToken(token.getValue());
        loginUserVO.setAccessTokenExpiresIn(token.getExpiresIn());
        loginUserVO.setRefreshToken(token.getRefreshToken().getValue());

        loginUserVO.setAppId(CLIENT_ID);
        loginUserVO.setAppSecret(CLIENT_SECRET);
        //存储登录的用户
        redisUtil.set(loginUserVO.getAccessToken(),loginUserVO,TimeUnit.HOURS.toSeconds(1));
        return loginUserVO;
    }

    @Override
    public LoginUserVO SSOLogin(LoginUserDTO loginUserDTO) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", loginUserDTO.getAppId());
        paramMap.add("client_secret", loginUserDTO.getAppSecret());
        paramMap.add("username", loginUserDTO.getUserName());
        paramMap.add("password", loginUserDTO.getPassword());
        paramMap.add("grant_type", GRANT_TYPE[0]);
        Token token = null;
        try {
            //因为oauth2本身自带的登录接口是"/oauth/token"，并且返回的数据类型不能按我们想要的去返回
            //但是我的业务需求是，登录接口是"user/login"，由于我没研究过要怎么去修改oauth2内部的endpoint配置
            //所以这里我用restTemplate(HTTP客户端)进行一次转发到oauth2内部的登录接口，比较简单粗暴
            token = restTemplate.postForObject(serverConfig.getUrl() + UrlEnum.LOGIN_URL.getUrl(), paramMap, Token.class);
            LoginUserVO loginUserVO = redisUtil.get(token.getValue(), LoginUserVO.class);
            if(loginUserVO != null){
                //登录的时候，判断该用户是否已经登录过了
                //如果redis里面已经存在该用户已经登录过了的信息
                //我这边要刷新一遍token信息，不然，它会返回上一次还未过时的token信息给你
                //不便于做单点维护
                token = oauthRefreshToken(loginUserVO.getRefreshToken());
                redisUtil.deleteCache(loginUserVO.getAccessToken());
            }
        } catch (RestClientException e) {
            try {
                e.printStackTrace();
                //此处应该用自定义异常去返回，在这里我就不去具体实现了
                //throw new Exception("username or password error");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        //这里我拿到了登录成功后返回的token信息之后，我再进行一层封装，最后返回给前端的其实是LoginUserVO
        LoginUserVO loginUserVO = new LoginUserVO();

        AuthUser userPO = authUserMapper.selectByUserName(loginUserDTO.getUserName());
        //TODO 补充缓存信息
        loginUserVO.setPassword(userPO.getPassword());
        loginUserVO.setUserName(userPO.getUser_name());
        loginUserVO.setAccessToken(token.getValue());
        loginUserVO.setAccessTokenExpiresIn(token.getExpiresIn());
        loginUserVO.setRefreshToken(token.getRefreshToken().getValue());
        loginUserVO.setAppId(loginUserDTO.getAppId());
        loginUserVO.setAppSecret(loginUserDTO.getAppSecret());
        //存储登录的用户
        redisUtil.set(loginUserVO.getAccessToken(),loginUserVO,TimeUnit.HOURS.toSeconds(1));
        return loginUserVO;
    }

    /**
     * @description oauth2客户端刷新token
     * @param refreshToken
     * @return
     */
    private Token oauthRefreshToken(String refreshToken) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", CLIENT_ID);
        paramMap.add("client_secret", CLIENT_SECRET);
        paramMap.add("refresh_token", refreshToken);
        paramMap.add("grant_type", GRANT_TYPE[1]);
        Token token = null;
        try {
            token = restTemplate.postForObject(serverConfig.getUrl() + UrlEnum.LOGIN_URL.getUrl(), paramMap, Token.class);
        } catch (RestClientException e) {
            try {
                //此处应该用自定义异常去返回，在这里我就不去具体实现了
                throw new Exception(ResponseEnum.REFRESH_TOKEN_INVALID.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return token;
    }


}
