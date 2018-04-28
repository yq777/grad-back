package com.grad.controller.portal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grad.common.Const;
import com.grad.common.ResponseCode;
import com.grad.common.ServerResponse;
import com.grad.model.ParamRequest;
import com.grad.pojo.User;
import com.grad.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *
 */
@Controller
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param session 存用户信息
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(@RequestBody ParamRequest request, HttpSession session) {
        JsonObject user = new JsonParser().parse(request.data).getAsJsonObject();
        ServerResponse<User> response = iUserService.login(user.get("username").getAsString(), user.get("password").getAsString());
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    // 退出登录
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    // 用户注册
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(@RequestBody ParamRequest request) {
        JsonObject jsonUser = new JsonParser().parse(request.data).getAsJsonObject();
        User user = new Gson().fromJson(jsonUser, User.class);
        return iUserService.register(user);
    }

    // 检查用户的邮箱或用户名是否已经存在
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(@RequestBody ParamRequest request) {
        JsonObject checkmsg = new JsonParser().parse(request.data).getAsJsonObject();
        return iUserService.checkValid(checkmsg.get("str").getAsString(), checkmsg.get("type").getAsString());
    }

    // 获取用户信息
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
    }

    // 获取忘记密码的密保问题
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse forgetGetQuestion(@RequestBody ParamRequest request) {
        JsonObject user = new JsonParser().parse(request.data).getAsJsonObject();
        return iUserService.selectQuestion(user.get("username").getAsString());
    }

    // 检查密保答案
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(@RequestBody ParamRequest request) {
        JsonObject user = new JsonParser().parse(request.data).getAsJsonObject();
        return iUserService.checkAnswer(user.get("username").getAsString(), user.get("question").getAsString(), user.get("answer").getAsString());
    }

    // 设置忘记密码的密码
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(@RequestBody ParamRequest request) {
        JsonObject user = new JsonParser().parse(request.data).getAsJsonObject();
        return iUserService.forgetRestPassword(user.get("username").getAsString(), user.get("passwordNew").getAsString(), user.get("forgetToken").getAsString());
    }

    // 重置密码
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, @RequestBody ParamRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        JsonObject userInfo = new JsonParser().parse(request.data).getAsJsonObject();
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        ServerResponse<String> value = iUserService.resetPassword(userInfo.get("passwordOld").getAsString(), userInfo.get("passwordNew").getAsString(), user);
        session.setAttribute(Const.CURRENT_USER, null);
        return value;
    }

    // 更新用户信息
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject jsonUser = new JsonParser().parse(request.data).getAsJsonObject();
        User user = new Gson().fromJson(jsonUser, User.class);
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    // 获取用户信息
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录或登录已过期");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
