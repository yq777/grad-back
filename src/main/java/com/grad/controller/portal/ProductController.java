package com.grad.controller.portal;

import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grad.common.ServerResponse;
import com.grad.model.ParamRequest;
import com.grad.pojo.Product;
import com.grad.service.IProductService;
import com.grad.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<Product> getProductDetail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestBody ParamRequest request) {
        JsonObject obj = new JsonParser().parse(request.getData()).getAsJsonObject();
        String keyword = obj.get("keyword").getAsString();
        // 查询所有商品的时候前台传-1过来
        Integer categoryId = obj.get("categoryId").getAsInt() == -1 ? null : obj.get("categoryId").getAsInt();
        Integer pageSize = obj.get("pageSize").getAsInt();
        Integer nowPage = obj.get("nowPage").getAsInt();
        String orderBy = obj.get("orderBy").getAsString();
        return iProductService.getProductByKeywordCategoryId(keyword, categoryId, pageSize, nowPage, orderBy);
    }
}
