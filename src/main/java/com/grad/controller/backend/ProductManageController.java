package com.grad.controller.backend;

import com.google.common.collect.Maps;
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
import com.grad.pojo.Product;
import com.grad.pojo.User;
import com.grad.service.IFileService;
import com.grad.service.IProductService;
import com.grad.service.IUserService;
import com.grad.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/manage/product/")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        BigDecimal price = obj.get("price").getAsBigDecimal();
        Product product = new Gson().fromJson(obj, Product.class);
        product.setPrice(price);
        User user = (User) session.getAttribute(Const.CURRENT_MANAGER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        Integer productId = obj.get("productId").getAsInt();
        Integer status = obj.get("status").getAsInt();
        User user = (User) session.getAttribute(Const.CURRENT_MANAGER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse setDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_MANAGER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getProductList(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        Integer pageSize = obj.get("pageSize").getAsInt();
        Integer nowPage = obj.get("nowPage").getAsInt();
        User user = (User) session.getAttribute(Const.CURRENT_MANAGER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.getProductList(pageSize, nowPage);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, @RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        Integer pageSize = obj.get("pageSize").getAsInt();
        Integer nowPage = obj.get("nowPage").getAsInt();
        String name = obj.get("name").getAsString();
        User user = (User) session.getAttribute(Const.CURRENT_MANAGER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "管理员未登录或登录已过期");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.searchProduct(pageSize, nowPage, name);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session, MultipartFile file, HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix" + targetFileName);
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    // 删除品类
    @RequestMapping(value = "delete_product.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse deleteProduct(HttpSession session, @RequestBody ParamRequest request) {
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
            return iProductService.deleteProducts(ids);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }
}
