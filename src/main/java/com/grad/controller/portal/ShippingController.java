package com.grad.controller.portal;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grad.common.Const;
import com.grad.common.ResponseCode;
import com.grad.common.ServerResponse;
import com.grad.model.ParamRequest;
import com.grad.pojo.Shipping;
import com.grad.pojo.User;
import com.grad.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/shipping")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        Shipping shipping = new Gson().fromJson(obj, Shipping.class);
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse delete(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        Integer shippingId = obj.get("id").getAsInt();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iShippingService.delete(user.getId(), shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        Shipping shipping = new Gson().fromJson(obj, Shipping.class);
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<PageInfo> select(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        Integer pageSize = obj.get("pageSize").getAsInt();
        Integer nowPage = obj.get("nowPage").getAsInt();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iShippingService.select(pageSize, nowPage, user.getId());
    }
}
