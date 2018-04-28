package com.grad.controller.portal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grad.common.Const;
import com.grad.common.ResponseCode;
import com.grad.common.ServerResponse;
import com.grad.model.ParamRequest;
import com.grad.pojo.User;
import com.grad.service.ICartService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api/cart/")
public class CarController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        Integer count = obj.get("count").getAsInt();
        Integer productId = obj.get("productId").getAsInt();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iCartService.add(user.getId(), count, productId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        Integer productId = obj.get("productId").getAsInt();
        Integer count = obj.get("count").getAsInt();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse delete(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        String cartIds = obj.get("productIds").getAsString();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iCartService.delete(user.getId(), cartIds);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("confirm.do")
    @ResponseBody
    public ServerResponse confirm(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject object = new JsonParser().parse(request.getData()).getAsJsonObject();
        String productIds = object.get("productIds").getAsString();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iCartService.confirm(user.getId(), productIds);
    }

    @RequestMapping("get_checked_product.do")
    @ResponseBody
    public ServerResponse getCheckedProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录或登录已过期");
        }
        return iCartService.getCheckedProduct(user.getId());
    }
}
