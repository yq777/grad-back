package com.grad.controller.backend;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.grad.common.Const;
import com.grad.common.ResponseCode;
import com.grad.common.ServerResponse;
import com.grad.model.ParamRequest;
import com.grad.pojo.Category;
import com.grad.pojo.User;
import com.grad.service.ICategoryService;
import com.grad.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        User user = (User) session.getAttribute(Const.CURRENT_MANAGER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        // 校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            //增加处理分类的逻辑
            return iCategoryService.addCategory(obj.get("categoryName").getAsString(), obj.get("parentId").getAsInt());
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    // 更新categoryName

    @RequestMapping(value = "update_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        User user = (User) session.getAttribute(Const.CURRENT_MANAGER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //更新category
            return iCategoryService.updateCategoryName(obj.get("categoryId").getAsInt(), obj.get("categoryName").getAsString());
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    // 获得品类
    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Category>> getChildrenParallerlCategory(HttpSession session, @RequestBody ParamRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_MANAGER);
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getChildrenParallerlCategory(obj.get("parentId").getAsInt());
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    @RequestMapping(value = "get_index_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Category>> getCategory(@RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        return iCategoryService.getChildrenParallerlCategory(obj.get("parentId").getAsInt());
    }

    // 删除品类
    @RequestMapping(value = "delete_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse deleteCategory(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        JsonArray categoryIds = obj.getAsJsonArray("ids");
        Gson gson = new Gson();
        List<Integer> ids = new ArrayList<>();
        for (JsonElement categoryId : categoryIds) {
            Integer id = gson.fromJson(categoryId, new TypeToken<Integer>() {
            }.getType());
            ids.add(id);
        }
        User user = (User) session.getAttribute(Const.CURRENT_MANAGER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.deleteCategory(ids);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }
}
