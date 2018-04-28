package com.grad.controller.backend;

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

@Controller
@RequestMapping("/api/manage/user")
public class UserManageController {
    @Autowired
    private IUserService iUserService;

    // 管理员登录
    @RequestMapping(value = "manager_login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(@RequestBody ParamRequest request, HttpSession session) {
        JsonObject user = new JsonParser().parse(request.data).getAsJsonObject();
        ServerResponse<User> response = iUserService.login(user.get("username").getAsString(), user.get("password").getAsString());
        if (response.isSuccess()) {
            User manager = response.getData();
            if (manager.getRole() == Const.Role.ROLE_ADMIN) {
                // 说明登陆的是管理员
                session.setAttribute(Const.CURRENT_MANAGER, manager);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员无法登陆");
            }
        }
        return response;
    }

    // 管理员退出登录
    @RequestMapping(value = "manager_logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_MANAGER);
        return ServerResponse.createBySuccess();
    }

    // 获取管理员信息
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_MANAGER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
